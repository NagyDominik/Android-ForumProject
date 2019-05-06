package com.exam.forumproject.DAL;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.exam.forumproject.BE.ForumPost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FirebaseDALManager implements DataAccessLayerManager {
    private static final String TAG = "ForumProject Firebase";
    private FirebaseFirestore db;

    FirebaseDALManager(Context context) {
        this.db = FirebaseFirestore.getInstance();
        Log.d(TAG, "Firestore initialized");
        this.getAllForumPost();
    }

    @Override
    public String createForumPost(ForumPost post) {
        return null;
    }

    @Override
    public List<ForumPost> getAllForumPost() {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
        return null;
    }

    @Override
    public ForumPost getForumPostById(String id) {
        return null;
    }

    @Override
    public void updateForumPost(ForumPost post) {

    }

    @Override
    public void deleteForumPost(String id) {

    }

    @Override
    public void getUserById(String id) {

    }

    @Override
    public void updateProfilePicture() {

    }
}
