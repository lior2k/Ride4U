package com.r4u.ride4u;

import androidx.annotation.NonNull;

public class Post {

    private final String postID;
    private final String publisherID;
    private final String publisherFirstName;
    private final String publisherLastName;

    private String seats;
    private final String source;
    private final String destination;
    private final String leavingTime;
    private final String leavingDate;
    private final String description;



    public Post(String postID, String publisherID, String publisherFirstName, String publisherLastName, String seats , String source,
                String destination, String leavingTime, String leavingDate, String description) {
        this.postID = postID;
        this.publisherID = publisherID;
        this.publisherFirstName = publisherFirstName;
        this.publisherLastName = publisherLastName;

        this.seats = seats;
        this.source = source;
        this.destination = destination;
        this.leavingTime = leavingTime;
        this.leavingDate = leavingDate;
        this.description = description;
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
