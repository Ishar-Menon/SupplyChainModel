package com.e.ishar.supplychainmodel2;

/**
 * Created by user on 05-03-2019.
 */

public class userProfile {
    public String role;
    public String userEmail;
    public String userName;

    public userProfile(){

    }


    public userProfile(String role, String userEmail, String userName) {
        this.role = role;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
