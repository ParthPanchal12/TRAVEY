package com.example.sarthak.profilepage_travey;

import android.graphics.Bitmap;

/**
 * Created by sarthak on 19/3/16.
 */
public class ProfileClass {
    private int icon;
    private String title;
    private String description;


    public ProfileClass(int icon, String title, String description) {
        this.icon = icon;
        this.title = title;
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
