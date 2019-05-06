package com.exam.forumproject.DAL;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.support.annotation.Nullable;
import android.util.Log;

import com.exam.forumproject.BE.ForumPost;
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
    private ObservableArrayList<ForumPost> postList = new ObservableArrayList<>();

    FirebaseDALManager(Context context) {
        this.db = FirebaseFirestore.getInstance();
        Log.d(TAG, "Firestore initialized");
    }

    @Override
    public String createForumPost(ForumPost post) {
        return null;
    }

    @Override
    public ObservableArrayList<ForumPost> getAllForumPost() {
        db.collection("forumposts")
            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    List<ForumPost> tempList = new ArrayList<>();
                    for (DocumentSnapshot doc : value) {
                        ForumPost temp = new ForumPost();
                        temp.setId(doc.getId());
                        temp.setTitle(doc.getString("title"));
                        temp.setPostDate(doc.getString("postDate"));
                        if (doc.getData().containsKey("pictureID")) {
                            temp.setPictureID(doc.getString("pictureID"));
                        }
                        if (doc.getData().containsKey("description")) {
                            temp.setPictureID(doc.getString("description"));
                        }
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
