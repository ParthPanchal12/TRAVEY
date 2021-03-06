package com.example.sarthak.navigationdrawer.LeaderBoard;

import com.example.sarthak.navigationdrawer.Backend.Backend.History;

import java.util.ArrayList;

/**
 * Created by sarthak on 7/4/16.
 */
public class User implements Comparable {
    public ArrayList<History> history;
    private String user_name;
    private int upvotes;
    private int downvotes;
    private String phone_number;
    private double location[]=new double[2];
    private double lat;
    private double longitude=72.6275615;

    public User() {
    }

    public User(String user_name, double latitude, double longitude) {
        this.user_name = user_name;
        this.lat = latitude;
        this.longitude = longitude;
    }

    public User(String name, int upvote, int downvote) {
        this.user_name = name;
        this.upvotes = upvote;
        this.downvotes = downvote;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public double getLatitude() {
        return lat;
    }

    public void setLatitude(double latitude) {
        this.lat = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public void getCoordinates(){
        this.lat=location[0];
        this.longitude=location[1];
    }

    @Override
    public int compareTo(Object s) {
        User o = (User) s;
        if (o.getUpvote() > this.getUpvote()) {
            return 1;
        }
        if (o.getUpvote() == this.getUpvote() && o.getDownvote() < this.getDownvote()) {
            return 1;
        }

        return -1;
    }
}
