package com.exam.forumproject.gui;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import com.exam.forumproject.be.ForumPost;
import com.exam.forumproject.dal.DALManagerFactory;
import com.exam.forumproject.dal.DataAccessLayerManager;

class Model {
    private static Model instance;
    private static final String TAG = "ForumProject GUI";

    private DataAccessLayerManager dalManager;
    private ObservableArrayList<ForumPost> forumPostsList;

    private Model(Context context) {
        DALManagerFactory.init(context);
        Log.d(TAG, "Model DALFactory initialized");
        if (dalManager == null) {
            dalManager = DALManagerFactory.getInstance();
        }
        forumPostsList = (ObservableArrayList<ForumPost>) dalManager.getAllForumPost();
        forumPostsList.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<ForumPost>>() {
            @Override
            public void onChanged(ObservableList<ForumPost> sender) {
                forumPostsList.clear();
                forumPostsList.addAll(sender);
                Log.d(TAG, "" + sender);
            }

            @Override
            public void onItemRangeChanged(ObservableList<ForumPost> sender, int positionStart, int itemCount) {
                Log.d(TAG, "" + sender);
            }

            @Override
            public void onItemRangeInserted(ObservableList<ForumPost> sender, int positionStart, int itemCount) {
                Log.d(TAG, "" + sender);
            }

            @Override
            public void onItemRangeMoved(ObservableList<ForumPost> sender, int fromPosition, int toPosition, int itemCount) {
                Log.d(TAG, "" + sender);
            }

            @Override
            public void onItemRangeRemoved(ObservableList<ForumPost> sender, int positionStart, int itemCount) {
                Log.d(TAG, "" + sender);
            }
        });
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
