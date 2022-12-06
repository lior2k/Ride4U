package com.r4u.ride4u;

public class Post {

    private final String publisher;
    private final String postID;
    private final String description;
    private final String source;
    private final String destination;

    public Post(String publisher, String postID, String description, String source, String destination) {
        this.publisher = publisher;
        this.postID = postID;
        this.description = description;
        this.source = source;
        this.destination = destination;
    }

    public String getPublisher() {
        return publisher;
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
}
