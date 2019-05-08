package com.exam.forumproject.DAL;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.exam.forumproject.BE.ForumPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDALManager implements DataAccessLayerManager {
    private static final String TAG = "ForumProject Firebase";
    private final long ONE_MEGABYTE = 1024 * 1024;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ObservableList<ForumPost> postList = new ObservableArrayList<>();
    private ObservableBoolean isLoading = new ObservableBoolean();
    private ObservableBoolean isPictureLoading = new ObservableBoolean();

    FirebaseDALManager(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
        Log.d(TAG, "Firestore initialized");
    }

    @Override
    public String createForumPost(ForumPost post) {

        return null;
    }

    @Override
    public ObservableList<ForumPost> getAllForumPost() {
        db.collection("forumposts").orderBy("postDate", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                isLoading.set(true);
                List<ForumPost> tempList = new ArrayList<>();
                for (DocumentSnapshot doc : value) {
                    ForumPost temp = doc.toObject(ForumPost.class);
                    temp.setId(doc.getId());
                    tempList.add(temp);
                }
                postList.clear();
                postList.addAll(tempList);
                isLoading.set(false);
                setPictures();
                Log.d(TAG, "Loaded posts: " + postList);
            }
        });
        return postList;
    }

    @Override
    public ForumPost getForumPostById(String id) {
        final ForumPost[] ret = new ForumPost[1];
        db.collection("forumposts").document(id).get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            ret[0] = document.toObject(ForumPost.class);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        return ret[0];
    }

    @Override
    public void deleteForumPost(String id) {
        db.collection("forumposts").document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Log.d(TAG, "post delete will be deleted with id:" + id);
                                db.collection("forumposts").document(id).delete();
                            }else {
                                Log.d(TAG, "No such document");
                            }
                        }else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void updateForumPost(ForumPost post) {

    }



    @Override
    public void getUserById(String id) {

    }

    @Override
    public void updateProfilePicture() {

    }

    @Override
    public ObservableBoolean isLoadingProperty() {
        return isLoading;
    }

    @Override
    public ObservableBoolean isPictureLoadingProperty() {
        return isPictureLoading;
    }

    private void setPictures() {
        isPictureLoading.set(true);
        StorageReference storageRef = storage.getReference();
        for (ForumPost post : postList) {
            if (post.getPictureID() != null && !post.getPictureID().equals("")) {
                storageRef.child("forumpost-pictures/" + post.getPictureID()).getDownloadUrl()
                    .addOnSuccessListener(uri -> {
                        post.setPictureUrl(uri.toString());
                        Log.d(TAG, "Picture loaded for: " + post);
                    })
                    .addOnFailureListener(error -> {
                        throw new IllegalArgumentException("No picture has been found with id: " + post.getPictureID());
                    })
                    .continueWith(task -> {
                        if (postList.lastIndexOf(post) == postList.size() - 1) {
                            isPictureLoading.set(false);
                        }
                        return null;
                    });
            }
        }
    }
}
