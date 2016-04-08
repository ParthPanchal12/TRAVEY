package com.example.sarthak.navigationdrawer.LeaderBoard;

import com.example.sarthak.navigationdrawer.Backend.Backend.History;

import java.util.ArrayList;

/**
 * Created by sarthak on 7/4/16.
 */
public class User {
    private String user_name;
    private int upvotes;
    private int downvotes;
    public ArrayList<History> history;
    private String phone_number;

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public User() {
    }

    public User(String name, int upvote, int downvote) {
        this.user_name = name;
        this.upvotes = upvote;
        this.downvotes = downvote;
    }

    public String getName() {
        return user_name;
    }

    public void setName(String name) {
        this.user_name = name;
    }

    public int getUpvote() {
        return upvotes;
    }

    public void setUpvote(int upvote) {
        this.upvotes = upvote;
    }

    public int getDownvote() {
        return downvotes;
    }

    public void setDownvote(int downvote) {
        this.downvotes = downvote;
    }
}
