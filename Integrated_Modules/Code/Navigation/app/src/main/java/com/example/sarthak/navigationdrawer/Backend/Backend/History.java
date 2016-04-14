package com.example.sarthak.navigationdrawer.Backend.Backend;

public class History {
    public String date;
    public String source;
    public String destination;

    public History() {
    }

    public History(String date, String source, String destination) {
        this.date = date;
        this.source = source;
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}