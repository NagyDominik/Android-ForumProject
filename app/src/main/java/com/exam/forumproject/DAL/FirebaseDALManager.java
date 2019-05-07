package com.exam.forumproject.DAL;

import android.content.Context;
import android.databinding.ObservableArrayList;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDALManager implements DataAccessLayerManager {
    private static final String TAG = "ForumProject Firebase";
    private FirebaseFirestore db;
    private ObservableList<ForumPost> postList = new ObservableArrayList<>();
    private ForumPost tempPost;

    FirebaseDALManager(Context context) {
        this.db = FirebaseFirestore.getInstance();
        Log.d(TAG, "Firestore initialized");
    }

    @Override
    public String createForumPost(ForumPost post) {
        return null;
    }

    @Override
    public ObservableList<ForumPost> getAllForumPost() {
        db.collection("forumposts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                List<ForumPost> tempList = new ArrayList<>();
                for (DocumentSnapshot doc : value) {
                    ForumPost temp = doc.toObject(ForumPost.class);
                    temp.setId(doc.getId());
                    tempList.add(temp);
                }
                postList.clear();
                postList.addAll(tempList);
                Log.d(TAG, "" + postList);
            }
        });
        return postList;
    }

    @Override
    public ForumPost getForumPostById(String id) {
        db.collection("cities").document("SF").get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            tempPost = document.toObject(ForumPost.class);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        ForumPost ret = tempPost;
        tempPost = null;
        return ret;
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
