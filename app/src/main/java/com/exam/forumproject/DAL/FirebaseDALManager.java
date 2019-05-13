package com.exam.forumproject.DAL;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.util.Log;

import com.exam.forumproject.BE.ForumPost;
import com.exam.forumproject.BE.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;

import javax.annotation.Nullable;

public class FirebaseDALManager implements DataAccessLayerManager {
    private static final String TAG = "ForumProject Firebase";
    private final String defaultUserID = "1ffKvrnr7lj4Gu0TzpBE"; //Temporary until no Auth
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private FirebaseForumPostManager forumPostManager;
    private FirebaseFileManager fileManager;
    private User defaultUser;
    private ObservableList<ForumPost> postList = new ObservableArrayList<>();

    FirebaseDALManager(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
        this.fileManager = new FirebaseFileManager(this.db, this.storage, this.postList);
        this.forumPostManager = new FirebaseForumPostManager(this.db, this.fileManager, this.postList);

        setUpListeners();
        defaultUser = getUserById("");
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
    public User getUserById(String userID) {
        this.db.collection("users").document(this.defaultUserID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {
                    Log.d(TAG, "User have found with id:" + documentSnapshot.getId());
                    defaultUser = documentSnapshot.toObject(User.class);
                    defaultUser.setId(documentSnapshot.getId());
                    fileManager.setUserProfilePic(defaultUser);
                } else {
                    Log.d(TAG, "No such document" + documentSnapshot.getId());
                }
            }
        });
        return defaultUser;
    }

    @Override
    public void updateProfilePicture(Bitmap bitmap) {
        this.fileManager.uploadImage(bitmap, "profile");
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
