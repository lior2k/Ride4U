package com.r4u.ride4u.Objects;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class User {

    private String id;
    private String firstname;
    private String lastname;
    private String email;
//    private final ArrayList<Post> ride_history;
    private final String Uid;
    private final Boolean isAdmin;

    public User(String firstname, String lastname, String email, String id, Boolean isAdmin, String u_id) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.id = id;
        this.isAdmin = isAdmin;
        this.Uid = u_id;
//        this.ride_history = new ArrayList<>();
    }
    public String getUid() { return Uid; }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public ArrayList<Post> getRideHistory() {return this.ride_history;}

    @NonNull
    public String toString() {
        return "[id: "+id+", firstname: "+firstname+", lastname: "+lastname+", email: "+email+"]";
    }

}
