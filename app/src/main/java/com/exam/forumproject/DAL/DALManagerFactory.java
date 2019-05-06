package com.exam.forumproject.dal;

import android.content.Context;
import android.util.Log;

public class DALManagerFactory {
    private static DataAccessLayerManager mInstance;
    private static final String TAG = "ForumProject DALFactory";

    /**
     * Creates a new singleton instance of the DataAccessLayerManager class
     *
     * @param context The context of the initializing activity
     */
    public static void init(Context context) {
        mInstance = new FirebaseDALManager(context);
        Log.d(TAG, "Factory initialized");
    }

    /**
     * Returns the singleton instance of the DataAccessLayerManager class created by the init() method
     *
     * @return A singleton instance of the DataAccessLayerManager class
     */
    public static DataAccessLayerManager getInstance() {
        return mInstance;
    }
}
