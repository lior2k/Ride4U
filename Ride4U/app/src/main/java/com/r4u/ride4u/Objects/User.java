package com.r4u.ride4u.Objects;

import androidx.annotation.NonNull;

import com.r4u.ride4u.UserActivities.Login;


public class User {

    private final String id;
    private final String firstname;
    private final String lastname;
    private final String email;
    private final String Uid;
    private final Boolean isAdmin;
    private final String deviceToken;

    public User(String firstname, String lastname, String email, String id, Boolean isAdmin, String u_id, String deviceToken) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.id = id;
        this.isAdmin = isAdmin;
        this.Uid = u_id;
        this.deviceToken = deviceToken;
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

    public String getDeviceToken(){return deviceToken;}

    public String getFullName() {
        return getFirstname() + " " + getLastname();
    }

    @NonNull
    public String toString() {
        return "[id: "+id+", firstname: "+firstname+", lastname: "+lastname+", email: "+email+"]";
    }

}