package com.death.tnt.signup;

public class EmailSignupModule {
    private String emailuserid;
    private String name;
    private String email;

    public String getEmailuserid() {
        return emailuserid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String firstname;
    private String lastname;
    private String profilepictureurl;

    public String getEmailuserid(String userID) {
        return emailuserid;
    }

    public void setEmailuserid(String emailuserid) {
        this.emailuserid = emailuserid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getProfilepictureurl() {
        return profilepictureurl;
    }

    public void setProfilepictureurl(String profilepictureurl) {
        this.profilepictureurl = profilepictureurl;
    }
}
