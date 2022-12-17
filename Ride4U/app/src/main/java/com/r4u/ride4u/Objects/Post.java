package com.r4u.ride4u.Objects;
import java.util.ArrayList;
import java.util.List;

public class Post {

    private final String postID;
    private final String publisherID;
    private final String publisherFirstName;
    private final String publisherLastName;

    private final String seats;
    private final String source;
    private final String destination;
    private final String leavingTime;
    private final String leavingDate;
    private final String cost;
    private final String description;

    private final List<String> passengerIDs;

    public Post(String postID, String publisherID, String publisherFirstName, String publisherLastName, String seats , String source,
                String destination, String leavingTime, String leavingDate, String cost,String description) {

        this.postID = postID;
        this.publisherID = publisherID;
        this.publisherFirstName = publisherFirstName;
        this.publisherLastName = publisherLastName;

        this.seats = seats;
        this.source = source;
        this.destination = destination;
        this.leavingTime = leavingTime;
        this.leavingDate = leavingDate;
        this.cost = cost;
        this.description = description;

        this.passengerIDs = new ArrayList<>();
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

    public String getCost() {return cost;}

    // The initial amount of seats available for passengers.
    public String getSeats() {
        return seats;
    }

    // current amount of available spots for passengers.
    public String getAvailableSeats() {
        return String.valueOf(Integer.parseInt(seats) - passengerIDs.size());
    }

    public String getLeavingTime() {
        return leavingTime;
    }

    public List<String> getPassengerIDs() {
        return this.passengerIDs;
    }

    public boolean addPassenger(String passengerID) {
        // Can't be a passenger of your own ride.
        if (passengerID.equals(publisherID)) {
            return false;
        }
        if (isFull()) {
            return false;
        }
        for (String id : passengerIDs) {
            if (id.equals(passengerID)) {
                return false;
            }
        }
        passengerIDs.add(passengerID);
        return true;
    }

    public String getPublisherFullName() {
        return getPublisherFirstName()+" "+getPublisherLastName();
    }

    // drive is full - signed up passengers are equal to the amount of available seats of the drive.
    public boolean isFull() {
        return passengerIDs.size() == Integer.parseInt(seats);
    }

    @Override
    public String toString() {
        return getPublisherFullName();
    }

}
