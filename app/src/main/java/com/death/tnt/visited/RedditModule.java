package com.death.tnt.visited;

import android.content.Intent;

public class RedditModule {
    private String image_url;
    private String caption;
    private String comment_link;
    private int no_of_comments;
    private int score;

    public int getNo_of_comments() {
        return no_of_comments;
    }

    public void setNo_of_comments(int no_of_comments) {
        this.no_of_comments = no_of_comments;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment_link() {
        return comment_link;
    }

    public void setComment_link(String comment_link) {
        this.comment_link = comment_link;
    }


    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


}
