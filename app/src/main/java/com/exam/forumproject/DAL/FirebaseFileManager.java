package com.exam.forumproject.DAL;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.exam.forumproject.BE.ForumPost;
import com.exam.forumproject.Utility.Utility;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.Date;

class FirebaseFileManager {
    private static final String TAG = "ForumProject FireFile";

    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private ObservableList<ForumPost> postList;
    private ObservableBoolean isPictureLoading = new ObservableBoolean(true);

    FirebaseFileManager(FirebaseFirestore db, FirebaseStorage storage, ObservableList<ForumPost> postList) {
        this.db = db;
        this.storage = storage;
        this.postList = postList;
    }

    String uploadImage(Bitmap image, String location) {
        String path;
        if (location.equals("forum")) {
            path = "forumpost-pictures/";
        } else {
            path = "profile-pictures/";
        }
        String uid = this.db.collection("files").document().getId();
        Log.d(TAG, "Generated id: " + uid);

        StorageReference pictureRef = storage.getReference().child(path + uid);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] pictureData = baos.toByteArray();

        StorageMetadata pictureMeta = new StorageMetadata.Builder()
            .setContentType("image/jpg")
            .setCustomMetadata("originalName", "IMG_" + Utility.dateFormater.format(new Date()) + "_Android")
            .build();

        pictureRef.putBytes(pictureData, pictureMeta)
            .addOnSuccessListener(taskSnapshot -> {
                Log.d(TAG, "Picture uploaded with name: " + uid);
                setPictures();
            })
            .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

        return uid;
    }

    void setPictures() {
        isPictureLoading.set(true);
        StorageReference storageRef = storage.getReference();
        for (int i = 0; i < postList.size(); i++) {
            ForumPost post = postList.get(i);
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
                            Handler handler = new Handler();
                            handler.postDelayed(() -> isPictureLoading.set(false), 300);
                        }
                        return null;
                    });
            }
        }
    }

    ObservableBoolean isPictureLoadingProperty() {
        return isPictureLoading;
    }
}
