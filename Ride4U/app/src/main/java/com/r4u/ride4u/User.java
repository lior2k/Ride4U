package com.r4u.ride4u;

import java.util.ArrayList;

public class User {

    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private final ArrayList<Post> myPosts;

    public User(String fname, String lname, String email, String id) {
        firstname = fname;
        lastname = lname;
        this.email = email;
        this.id = id;
        myPosts = new ArrayList<>();
    }

    public User() {
        myPosts = new ArrayList<>();
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

    public ArrayList<Post> getMyPosts() {
        return myPosts;
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

    public void addPost(Post p) {
        myPosts.add(p);
    }
}
