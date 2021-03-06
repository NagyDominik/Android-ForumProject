package com.exam.forumproject.DAL;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.graphics.Bitmap;

import com.exam.forumproject.BE.ForumPost;
import com.exam.forumproject.BE.User;

public interface DataAccessLayerManager {

    /**
     * Creates a new forum post in the database.
     *
     * @param post The new post to be created.
     * @return The id of the created forum post.
     */
    void createForumPost(ForumPost post, Bitmap bitmap);

    /**
     * Returns all forum posts from the database in chronological order.
     *
     * @return A list of ForumPost representing the posts in the database.
     */
    ObservableList<ForumPost> getAllForumPost();

    /**
     * Returns the post, with matching id, from the database.
     *
     * @param id The id of the ForumPost.
     */
    ForumPost getForumPostById(String id);

    /**
     * Deletes the forum post, with matching id from the database.
     *
     * @param id The id of the forum post.
     */
    void deleteForumPost(String id);

    /**
     * Returns the user, with matching id, from the database.
     *
     * @param id The id of the user.
     */
    User getUserById(String id);

    /**
     * Returns the user, with matching id, from the database.
     *
     * @param bitmap The new profile picture.
     */
    void updateProfilePicture(Bitmap bitmap);

    /**
     * Returns an observable boolean which represents the state of the post loading.
     *
     * @return True if the loading is in progress,
     * False if the loading has finished
     */
    ObservableBoolean isLoadingProperty();

    /**
     * Returns an observable boolean which represents the state of the post picture loading.
     *
     * @return True if the loading is in progress,
     * False if the loading has finished
     */
    ObservableBoolean isPictureLoadingProperty();

}
