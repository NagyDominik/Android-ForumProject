package com.exam.forumproject.DAL;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.exam.forumproject.BE.ForumPost;
import com.exam.forumproject.BE.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class FirebaseDALManager implements DataAccessLayerManager {
    private static final String TAG = "ForumProject Firebase";
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseForumPostManager forumPostManager;
    private FirebaseFileManager fileManager;
    private ObservableList<ForumPost> postList = new ObservableArrayList<>();

    FirebaseDALManager(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.fileManager = new FirebaseFileManager(this.db, this.storage, this.postList);
        this.forumPostManager = new FirebaseForumPostManager(this.db, this.fileManager, this.postList);

        setUpListeners();

        Log.d(TAG, "Firestore initialized");
    }

    @Override
    public void createForumPost(ForumPost post, Bitmap bitmap) {
        if (bitmap != null) {
            forumPostManager.createPostWithImage(post, bitmap);
        } else if (post.getDescription() != null && !post.getDescription().equals("")) {
            forumPostManager.createTextPost(post);
        } else {
            throw new IllegalArgumentException("Picture or text is missing!");
        }
    }

    @Override
    public ObservableList<ForumPost> getAllForumPost() {
        this.forumPostManager.getAllForumPost();
        return this.postList;
    }

    @Override
    public ForumPost getForumPostById(String id) {
        return this.forumPostManager.getForumPostById(id);
    }

    @Override
    public void deleteForumPost(String id) {
        this.forumPostManager.deleteForumPost(id);
    }

    @Override
    public void updateForumPost(ForumPost post) {

    }


    @Override
    public User getUserById(String userID) {
        User user = new User();
        this.db.collection("users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    if (document.exists()) {
                        Log.d(TAG, "User have found with id:" + userID);
                        user.setId(document.getId());
                    } else {
                        Log.d(TAG, "No such document" + document.getId());
                    }
                } else {
                    Log.d(TAG, "Get failed with ", task.getException());
                }
            }
        });
        return user;
    }

    @Override
    public void updateProfilePicture() {

    }

    @Override
    public ObservableBoolean isLoadingProperty() {
        return this.forumPostManager.isLoadingProperty();
    }

    @Override
    public ObservableBoolean isPictureLoadingProperty() {
        return this.fileManager.isPictureLoadingProperty();
    }

    private void setUpListeners() {
        this.forumPostManager.getPicRefreshProperty().addOnPropertyChangedCallback(
            new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    fileManager.setPictures();
                }
            });
    }
}
