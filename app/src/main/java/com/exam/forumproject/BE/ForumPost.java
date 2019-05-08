package com.exam.forumproject.BE;

public class ForumPost {
    private String id;
    private String title;
    private String postDate;
    private String description;
    private String pictureID;
    private String pictureUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureID() {
        return pictureID;
    }

    public void setPictureID(String pictureID) {
        this.pictureID = pictureID;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    @Override
    public String toString() {
        return "ForumPost{" +
            "id='" + id + '\'' +
            ", title='" + title + '\'' +
            ", postDate='" + postDate + '\'' +
            ", description='" + description + '\'' +
            ", pictureID='" + pictureID + '\'' +
            ", pictureUrl='" + pictureUrl + '\'' +
            '}';
    }
}
