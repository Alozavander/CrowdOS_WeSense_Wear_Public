package com.example.wesense_wearos.networkClasses;

public class UserAccount_GetDataParse extends GetDataParse {
    private String id;
    private String name;
    private String pwd;
    private String phone;
    private String email;
    private String Sex;
    private String Tag;

    public UserAccount_GetDataParse(){
        super();
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPwd() {
        return pwd;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getSex() {
        return Sex;
    }

    public String getTag() {
        return Tag;
    }
}
