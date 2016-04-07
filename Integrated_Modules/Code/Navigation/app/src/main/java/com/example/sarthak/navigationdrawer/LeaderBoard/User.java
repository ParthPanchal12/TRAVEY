package com.example.sarthak.navigationdrawer.LeaderBoard;

/**
 * Created by sarthak on 7/4/16.
 */
public class User {
    private String name;
    private int upvote;
    private int downvote;

    public User(String name, int upvote, int downvote) {
        this.name = name;
        this.upvote = upvote;
        this.downvote = downvote;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUpvote() {
        return upvote;
    }

    public void setUpvote(int upvote) {
        this.upvote = upvote;
    }

    public int getDownvote() {
        return downvote;
    }

    public void setDownvote(int downvote) {
        this.downvote = downvote;
    }
}
