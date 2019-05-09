package com.exam.forumproject.GUI;

import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.exam.forumproject.BE.ForumPost;
import com.exam.forumproject.DAL.DALManagerFactory;
import com.exam.forumproject.DAL.DataAccessLayerManager;

class Model {
    private static Model instance;
    private static final String TAG = "ForumProject Model";
    private static final int PERMISSION_REQUEST_CODE = 0;
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

    /**
     * Check if the application activity has the required permission
     *
     * @param activity   The application activity
     * @param permission The permission that is being checked
     * @return True if the permission is granted, False otherwise
     */
    boolean checkPermissions(AppCompatActivity activity, String permission) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                Log.d(TAG, "permission denied to action - requesting it");
                String[] permissions = {permission};

                activity.requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                return false;
            } else {
                Log.d(TAG, "permission to action granted!");
                return true;
            }
        }
        return true;
    }

    void deletePost(String id) {
        dalManager.deleteForumPost(id);
    }

    ObservableBoolean getIsLoading() {
        return dalManager.isLoadingProperty();
    }

    public ObservableBoolean getIsPictureLoading() {
        return dalManager.isLoadingProperty();
    }

    public ForumPost getForumPostById(String id) {
        return dalManager.getForumPostById(id);
    }

    ObservableList<ForumPost> getAllForumPost() {
        return this.forumPostsList;
    }

    public void createForumPost(ForumPost post, Bitmap bitmap){
        dalManager.createForumPost(post,bitmap);
    }
}
