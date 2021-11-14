package com.example.wesense_wearos.beans;

public class bean_listview_main {
    int Image;
    String name;

    public bean_listview_main(int pImage, String pName) {
        Image = pImage;
        name = pName;
    }

    public bean_listview_main() {
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int pImage) {
        Image = pImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }
}
