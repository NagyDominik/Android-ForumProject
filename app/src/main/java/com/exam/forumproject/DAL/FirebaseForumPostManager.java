package com.exam.forumproject.DAL;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;

import com.exam.forumproject.BE.ForumPost;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

class FirebaseForumPostManager {
    private static final String TAG = "ForumProject FirePost";
    private SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private ObservableBoolean isLoading = new ObservableBoolean();
    private ObservableBoolean picRefresh = new ObservableBoolean();
    private boolean firstTimeLoad = true;
    private ObservableList<ForumPost> postList;
    private FirebaseFileManager fileManager;
    private FirebaseFirestore db;

    FirebaseForumPostManager(FirebaseFirestore db, FirebaseFileManager fileManager, ObservableList<ForumPost> postList) {
        this.postList = postList;
        this.db = db;
        this.fileManager = fileManager;
        this.dateFormater.setTimeZone(TimeZone.getTimeZone("UTC"));
        Log.d(TAG, "FirePost initialized");
    }

    void createPostWithImage(ForumPost post, Bitmap bitmap) {
        Map<String, Object> postDTO = new HashMap<>();
        postDTO.put("title", post.getTitle());
        postDTO.put("postDate", dateFormater.format(new Date()));
        String picId = fileManager.uploadImage(bitmap, "forum");
        if (picId != null) {
            postDTO.put("pictureID", picId);
        }
        createForumDBEntry(postDTO);
    }

    void createTextPost(ForumPost post) {
        Map<String, Object> postDTO = new HashMap<>();
        postDTO.put("title", post.getTitle());
        postDTO.put("postDate", dateFormater.format(new Date()));
        if (post.getDescription() != null && !post.getDescription().equals("")) {
            postDTO.put("description", post.getDescription());
        }
        createForumDBEntry(postDTO);
    }

    private void createForumDBEntry(Object post) {
        db.collection("forumposts").add(post)
            .addOnSuccessListener(documentReference -> Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId()))
            .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));
    }

    ObservableList<ForumPost> getAllForumPost() {
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
                if (firstTimeLoad) {
                    picRefresh.notifyChange();
                }
                firstTimeLoad = false;

                Log.d(TAG, "Loaded posts: " + postList);
            }
        });
        return postList;
    }

    ForumPost getForumPostById(String id) {
        final ForumPost[] ret = new ForumPost[1];
        db.collection("forumposts").document(id).get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        ret[0] = document.toObject(ForumPost.class);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            });
        return ret[0];
    }

    void deleteForumPost(String id) {
        db.collection("forumposts").document(id).get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        Log.d(TAG, "post delete will be deleted with id:" + id);
                        db.collection("forumposts").document(id).delete();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "Get failed with ", task.getException());
                }
            });
    }

    ObservableBoolean isLoadingProperty() {
        return this.isLoading;
    }

    ObservableBoolean getPicRefreshProperty() {
        return this.picRefresh;
    }
}
