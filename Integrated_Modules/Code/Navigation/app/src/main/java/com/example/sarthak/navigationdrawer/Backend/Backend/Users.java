package com.example.sarthak.navigationdrawer.Backend.Backend;

import java.util.ArrayList;

/**
 * Created by Sneh_132 on 3/28/2016.
 */
public class Users {
    public String name;
    public ArrayList<History> history;
    public String password;
    public String emailId;
    public String _id;
    public String currLoc;
    public boolean sharingStatus;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<History> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<History> history) {
        this.history = history;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCurrLoc() {
        return currLoc;
    }

    public void setCurrLoc(String currLoc) {
        this.currLoc = currLoc;
    }

    public boolean isSharingStatus() {
        return sharingStatus;
    }

    public void setSharingStatus(boolean sharingStatus) {
        this.sharingStatus = sharingStatus;
    }
}
