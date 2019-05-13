package com.death.tnt;

public class DataModule {
    //user_account
    private String userid;
    private String name;
    private String firstname;
    private String lastname;
    private String profilepictureurl;
    private String phone;
    private String email;
    private String photourl;
    //feed
    private String caption;
    private String feedImageURL;
    private String timestamp;
    private String feedPhotoDowloadUrl;
    //user_info
    private String user_address;
    private String user_bio;
    private String user_likes;

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_bio() {
        return user_bio;
    }

    public void setUser_bio(String user_bio) {
        this.user_bio = user_bio;
    }

    public String getUser_likes() {
        return user_likes;
    }

    public void setUser_likes(String user_likes) {
        this.user_likes = user_likes;
    }

    public String getFeedPhotoDowloadUrl() {
        return feedPhotoDowloadUrl;
    }

    public void setFeedPhotoDowloadUrl(String feedPhotoDowloadUrl) {
        this.feedPhotoDowloadUrl = feedPhotoDowloadUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFeedImageURL() {
        return feedImageURL;
    }

    public void setFeedImageURL(String feedImageURL) {
        this.feedImageURL = feedImageURL;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
