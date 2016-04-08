package com.example.sarthak.navigationdrawer.ContactDisplay;

/**
 * Created by sarthak on 6/4/16.
 */
public class Friends {
    private String name;
    private String phone;

    public Friends() {
    }

    public Friends(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
