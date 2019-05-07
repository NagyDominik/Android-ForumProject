package com.exam.forumproject.GUI;

import android.content.Context;
import android.databinding.ObservableList;
import android.util.Log;

import com.exam.forumproject.BE.ForumPost;
import com.exam.forumproject.DAL.DALManagerFactory;
import com.exam.forumproject.DAL.DataAccessLayerManager;

import java.util.List;

class Model {
    private static Model instance;
    private static final String TAG = "ForumProject Model";
    private DataAccessLayerManager dalManager;
    private ObservableList<ForumPost> forumPostsList;

    private Model(Context context) {
        DALManagerFactory.init(context);
        Log.d(TAG, "Model DALFactory initialized");
        if (dalManager == null) {
            dalManager = DALManagerFactory.getInstance();
        }
        forumPostsList = dalManager.getAllForumPost();
        setUpListChangeListener();
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

    /**
     * Sets up the OnListChangeCallback of forumPostsList
     */
    private void setUpListChangeListener() {
        forumPostsList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<ForumPost>>() {
            @Override
            public void onChanged(ObservableList<ForumPost> sender) {
                forumPostsList = sender;
                Log.d(TAG, "onChanged: " + sender);
                Log.d(TAG, "forumPostsList: " + forumPostsList);
            }

            @Override
            public void onItemRangeChanged(ObservableList<ForumPost> sender, int positionStart, int itemCount) {
                forumPostsList = sender;
                Log.d(TAG, "onItemRangeChanged: " + sender);
                Log.d(TAG, "forumPostsList: " + forumPostsList);
            }

            @Override
            public void onItemRangeInserted(ObservableList<ForumPost> sender, int positionStart, int itemCount) {
                forumPostsList = sender;
                Log.d(TAG, "onItemRangeInserted: " + sender);
                Log.d(TAG, "forumPostsList: " + forumPostsList);
            }

            @Override
            public void onItemRangeMoved(ObservableList<ForumPost> sender, int fromPosition, int toPosition, int itemCount) {
                forumPostsList = sender;
                Log.d(TAG, "onItemRangeMoved: " + sender);
                Log.d(TAG, "forumPostsList: " + forumPostsList);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<ForumPost> sender, int positionStart, int itemCount) {
                forumPostsList = sender;
                Log.d(TAG, "onItemRangeRemoved: " + sender);
                Log.d(TAG, "forumPostsList: " + forumPostsList);
            }
        });
    }

    public ForumPost getForumPostById(String id) {
        return dalManager.getForumPostById(id);
    }
    public ObservableList<ForumPost> getAllForumPost(){
        return this.forumPostsList;
    }
}
