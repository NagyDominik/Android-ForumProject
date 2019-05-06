package com.exam.forumproject.dal;

import android.content.Context;

import com.exam.forumproject.be.ForumPost;

import java.util.List;

public class FirebaseDALManager implements DataAccessLayerManager {

    FirebaseDALManager(Context context) {
    }

    @Override
    public String createForumPost(ForumPost post) {
        return null;
    }

    @Override
    public List<ForumPost> getAllForumPost() {
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
