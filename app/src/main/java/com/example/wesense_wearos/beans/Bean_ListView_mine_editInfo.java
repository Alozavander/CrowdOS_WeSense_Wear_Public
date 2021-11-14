package com.example.wesense_wearos.beans;

public class Bean_ListView_mine_editInfo {
    private String title;
    private String content;

    public Bean_ListView_mine_editInfo(String title, String content) {
        this.title = title;
        this.content = content;
    }


    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
