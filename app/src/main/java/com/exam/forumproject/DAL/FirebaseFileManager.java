package com.exam.forumproject.DAL;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.exam.forumproject.BE.ForumPost;
import com.exam.forumproject.BE.User;
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

    /**
     * Uploads {@code image} to firebase with a path that depends on the {@code location}.
     *
     * @param image    The bitmap of the image to be uploaded
     * @param location The short form of the location. (forum or profile)
     * @return The id of the uploaded image.
     */
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

    /**
     * Sets the picture urls of each ForumPost in the local ObservableList reference supplied in the
     * constructor.
     */
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

    /**
     * Sets the profile picture url of the given user reference.
     *
     * @param user The user that requires profile picture
     */
    void setUserProfilePic(User user) {
        StorageReference storageRef = storage.getReference();
        storageRef.child("profile-pictures/" + user.getProfilePicId()).getDownloadUrl()
            .addOnSuccessListener(uri -> {
                user.setProfilePicUrl(uri.toString());
                Log.d(TAG, "Picture loaded for: " + user);
            })
            .addOnFailureListener(error -> {
                throw new IllegalArgumentException("No picture has been found with id: " + user.getProfilePicId());
            });
    }

    ObservableBoolean isPictureLoadingProperty() {
        return isPictureLoading;
    }
}
