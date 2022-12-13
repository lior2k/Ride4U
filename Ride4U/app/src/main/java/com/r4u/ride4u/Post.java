package com.r4u.ride4u;

import androidx.annotation.NonNull;

public class Post {

    private final String publisherID;
    private final String publisherFirstName;
    private final String publisherLastName;
    private final String postID;
    private final String description;
    private final String source;
    private final String destination;
    private String seats;
    private final String leavingTime;
    private final String leavingDate;

    public Post(String publisherID, String postID, String publisherFirstName, String publisherLastName, String description, String source,
                String destination, String seats, String leavingTime, String leavingDate) {
        this.publisherID = publisherID;
        this.postID = postID;
        this.publisherFirstName = publisherFirstName;
        this.publisherLastName = publisherLastName;
        this.description = description;
        this.source = source;
        this.destination = destination;
        this.seats = seats;
        this.leavingTime = leavingTime;
        this.leavingDate = leavingDate;
    }

    public String getPublisherID() {
        return publisherID;
    }

    public String getLeavingDate() {
        return leavingDate;
    }

    public String getPostID() {
        return postID;
    }

    public String getDescription() {
        return description;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getPublisherFirstName() {
        return publisherFirstName;
    }

    public String getPublisherLastName() {
        return publisherLastName;
    }

    public String getSeats() {
        return seats;
    }

    public String getLeavingTime() {
        return leavingTime;
    }

    public String decrementSeats() {
        seats = String.valueOf(Integer.parseInt(seats)-1);
        return seats;
    }

    @NonNull
    @Override
    public String toString() {
        return "[ID: "+publisherID+", firstname: "+publisherFirstName+", lastname: "+publisherLastName+", src: "+source+", dest:"+destination+"]";
    }

}
