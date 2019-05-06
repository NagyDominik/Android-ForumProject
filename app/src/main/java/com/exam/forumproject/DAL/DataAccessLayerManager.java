package com.exam.forumproject.dal;

import com.exam.forumproject.be.ForumPost;

import java.util.List;

public interface DataAccessLayerManager {

    /**
     * Creates a new forum post in the database.
     *
     * @param post The new post to be created.
     * @return The id of the created forum post.
     */
    String create(ForumPost post);

    /**
     * Returns all forum posts from the database in chronological order.
     *
     * @return A list of ForumPost representing the posts in the database.
     */
    List<ForumPost> getAll();

    /**
     * Returns the forum post with matching id from the database.
     *
     * @param id The id of the forum post.
     * @return The forum post with matching id.
     */
    ForumPost getById(String id);

    /**
     * Updates the forum post, with matching id, in the database.
     *
     * @param post The new data for the update.
     */
    void update(ForumPost post);

    /**
     * Deletes the forum post, with matching id from the database.
     *
     * @param id The id of the forum post.
     */
    void delete(String id);

}
