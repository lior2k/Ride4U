package com.r4u.ride4u;

import androidx.annotation.NonNull;

public class Post {

    private final String publisherID;
    private final String publisherFirstName;
    private final String publisherLastName;
//    private final String postID;
    private final String description;
    private final String source;
    private final String destination;

    public Post(String publisherID, String publisherFirstName, String publisherLastName, String description, String source, String destination) {
        this.publisherID = publisherID;
//        this.postID = postID;
        this.publisherFirstName = publisherFirstName;
        this.publisherLastName = publisherLastName;
        this.description = description;
        this.source = source;
        this.destination = destination;
    }

    public String getPublisherID() {
        return publisherID;
    }

//    public String getPostID() {
//        return postID;
//    }

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

    @NonNull
    @Override
    public String toString() {
        return "[ID: "+publisherID+", firstname: "+publisherFirstName+", lastname: "+publisherLastName+", src: "+source+", dest:"+destination+"]";
    }

}
