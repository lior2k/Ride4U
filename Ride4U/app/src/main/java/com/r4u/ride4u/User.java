package com.r4u.ride4u;

import androidx.annotation.NonNull;

public class User {

    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private final String Uid;
    private final Boolean isAdmin;

    public User(String fname, String lname, String email, String id, Boolean isAdmin, String u_id) {
        firstname = fname;
        lastname = lname;
        this.email = email;
        this.id = id;
        this.isAdmin = isAdmin;
        this.Uid = u_id;
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

    @NonNull
    public String toString() {
        return "[id: "+id+", firstname: "+firstname+", lastname: "+lastname+", email: "+email+"]";
    }

}
