package com.exam.forumproject.dal;

import android.content.Context;

import com.exam.forumproject.be.ForumPost;

import java.util.List;

public class FirebaseDALManager implements DataAccessLayerManager {

    public FirebaseDALManager(Context context) {
    }

    @Override
    public String create(ForumPost post) {
        return null;
    }

    @Override
    public List<ForumPost> getAll() {
        return null;
    }

    @Override
    public ForumPost getById(String id) {
        return null;
    }

    @Override
    public void update(ForumPost post) {

    }

    @Override
    public void delete(String id) {

    }
}
