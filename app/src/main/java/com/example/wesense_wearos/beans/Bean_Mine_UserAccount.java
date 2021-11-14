package com.example.wesense_wearos.beans;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Bean_Mine_UserAccount {
    private int usericon;
    private String username;
    private String usersign;
    private boolean loginStatue;

    public Bean_Mine_UserAccount(int pUsericon, String pUsername, String pUsersign) {
        usericon = pUsericon;
        username = pUsername;
        usersign = pUsersign;
    }

    public Bean_Mine_UserAccount(int pUsericon, String pUsername, String pUsersign, boolean pLoginStatue) {
        usericon = pUsericon;
        username = pUsername;
        usersign = pUsersign;
        loginStatue = pLoginStatue;
    }

    public int getUsericon() {
        return usericon;
    }

    public void setUsericon(int pUsericon) {
        usericon = pUsericon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String pUsername) {
        username = pUsername;
    }

    public String getUsersign() {
        return usersign;
    }

    public void setUsersign(String pUsersign) {
        usersign = pUsersign;
    }

    public boolean isLoginStatue() {
        return loginStatue;
    }

    public void setLoginStatue(boolean pLoginStatue) {
        loginStatue = pLoginStatue;
    }
}
