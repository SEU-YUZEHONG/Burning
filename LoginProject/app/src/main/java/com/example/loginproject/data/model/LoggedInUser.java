package com.example.loginproject.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 * 数据类用来抓取已登录用户的信息
 */
public class LoggedInUser {

    private String userId;
    private String displayName;

    public LoggedInUser(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }
}
