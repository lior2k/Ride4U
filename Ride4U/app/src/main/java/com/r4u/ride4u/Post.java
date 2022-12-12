package com.r4u.ride4u;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Post {

//    private final String postID;
    private final String source;
    private final String destination;
    private final String description;
    private String seats;
    private final String leavingTime;

    private final User driver;
    private final ArrayList<User> passengers;

    public Post(User driver, String source, String destination, String description, String seats, String leavingTime) {
        this.source = source;
        this.destination = destination;
        this.description = description;
        this.seats = seats;
        this.leavingTime = leavingTime;

        this.driver = driver;
        passengers = new ArrayList<>();
    }

    public String getPublisherID() {
        return driver.getId();
    }

    public String getPublisherFirstName() {
        return driver.getFirstname();
    }

    public String getPublisherLastName() {
        return driver.getLastname();
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    public String getSeats() {
        return seats;
    }

    public String getLeavingTime() {
        return leavingTime;
    }

    public ArrayList<User> getPassengers() {
        return passengers;
    }

    public void addPassenger(User passenger) {
        seats = String.valueOf(Integer.parseInt(seats)-1); // seats--
        passengers.add(passenger);
    }

    @NonNull
    @Override
    public String toString() {
        return "[ID: "+driver.getId()+", src: "+source+", dest:"+destination+"]";
    }

}
