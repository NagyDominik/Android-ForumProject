package com.exam.forumproject.dal;

import android.databinding.ObservableArrayList;

import com.exam.forumproject.be.ForumPost;

public interface DataAccessLayerManager {

    /**
     * Creates a new forum post in the database.
     *
     * @param post The new post to be created.
     * @return The id of the created forum post.
     */
    String createForumPost(ForumPost post);

    /**
     * Returns all forum posts from the database in chronological order.
     *
     * @return A list of ForumPost representing the posts in the database.
     */
    ObservableArrayList<ForumPost> getAllForumPost();

    /**
     * Returns the forum post with matching id from the database.
     *
     * @param id The id of the forum post.
     * @return The forum post with matching id.
     */
    ForumPost getForumPostById(String id);

    /**
     * Updates the forum post, with matching id, in the database.
     *
     * @param post The new data for the updateForumPost.
     */
    void updateForumPost(ForumPost post);

    /**
     * Deletes the forum post, with matching id from the database.
     *
     * @param id The id of the forum post.
     */
    void deleteForumPost(String id);

    /**
     * Returns the user, with matching id, from the database.
     *
     * @param id
     */
    void getUserById(String id);

    /**
     * Updates the users profile picture.
     */
    void updateProfilePicture();

}
