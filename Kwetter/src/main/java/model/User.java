package model;

import java.util.Set;

public class User {
    private int id;
    private String username;
    private String biography;
    private double locationLongitude;
    private double locationLatitude;
    private String website;
    private Set<User> followers;
    private Set<User> following;
    private Role role;

    public User(String username, String biography, double locationLongitude, double locationLatitude, String website, Role role) {
        this.username = username;
        this.biography = biography;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.website = website;
        this.role = role;
    }

    public User(int id, String username, String biography, double locationLongitude, double locationLatitude, String website, Role role) {
        this.id = id;
        this.username = username;
        this.biography = biography;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.website = website;
        this.role = role;
    }
}
