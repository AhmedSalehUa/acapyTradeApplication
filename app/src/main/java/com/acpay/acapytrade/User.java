package com.acpay.acapytrade;

public class User {
    public String username;
    public String userId;
     private String status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String userId ) {
        this.username = username;
        this.userId = userId;

    }

    public User(String username, String userId, String status) {
        this.username = username;
        this.userId = userId;

        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
