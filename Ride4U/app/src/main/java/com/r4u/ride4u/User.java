package com.r4u.ride4u;

public class User {

    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private Boolean isAdmin;

    public User(String fname, String lname, String email, String id, Boolean isAdmin) {
        firstname = fname;
        lastname = lname;
        this.email = email;
        this.id = id;
        this.isAdmin = isAdmin;
    }

    public User() {}

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

    public String toString() {
        return "[id: "+id+", firstname: "+firstname+", lastname: "+lastname+", email: "+email+"]";
    }
}
