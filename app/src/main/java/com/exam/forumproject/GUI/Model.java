package com.exam.forumproject.GUI;

import android.content.Context;
import android.util.Log;

import com.exam.forumproject.BE.ForumPost;
import com.exam.forumproject.DAL.DALManagerFactory;
import com.exam.forumproject.DAL.DataAccessLayerManager;

import java.util.ArrayList;
import java.util.List;

class Model {
    private static Model instance;
    private static final String TAG = "ForumProject GUI";

    private DataAccessLayerManager dalManager;
    private List<ForumPost> forumPostsList = new ArrayList<>();

    private Model(Context context) {
        DALManagerFactory.init(context);
        Log.d(TAG, "Model DALFactory initialized");
        if (dalManager == null) {
            dalManager = DALManagerFactory.getInstance();
        }
        forumPostsList.addAll(dalManager.getAllForumPost());
    }

    /**
     * Returns a singleton instance of the Model class
     *
     * @param context The context of the default activity
     * @return A singleton instance of the Model class
     */
    static Model getInstance(Context context) {
        if (instance == null) {
            instance = new Model(context);
            Log.d(TAG, "Model created");
        }
        return instance;
    }
}
