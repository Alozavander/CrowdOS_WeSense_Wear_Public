package com.example.wesense_wearos.beans;


public class Bean_ListView_mine {
    private int icon;
    private String title;

    public Bean_ListView_mine(int icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
