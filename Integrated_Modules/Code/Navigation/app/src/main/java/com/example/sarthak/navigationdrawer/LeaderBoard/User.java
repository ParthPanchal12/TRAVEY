package com.example.sarthak.navigationdrawer.LeaderBoard;

/**
 * Created by sarthak on 7/4/16.
 */
public class User {
    private String user_name;
    private int upvotes;
    private int downvotes;

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
