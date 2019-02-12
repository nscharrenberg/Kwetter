/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package model;

import exception.StringToLongException;
import exception.UsernameNotUniqueException;

import java.util.HashSet;
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

    public User(String username, String biography, double locationLongitude, double locationLatitude, String website, Role role) throws StringToLongException, UsernameNotUniqueException {
        //TODO: Add check for unique username

        if(biography.length() > 160) {
            throw new StringToLongException("Biography can not be more then 160 characters long.");
        }

        this.username = username;
        this.biography = biography;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.website = website;
        this.role = role;
        this.followers = new HashSet<User>();
        this.following = new HashSet<User>();
    }

    public User(int id, String username, String biography, double locationLongitude, double locationLatitude, String website, Role role) throws StringToLongException, UsernameNotUniqueException {
        //TODO: Add check for unique username

        if(biography.length() > 160) {
            throw new StringToLongException("Biography can not be more then 160 characters long.");
        }

        this.id = id;
        this.username = username;
        this.biography = biography;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.website = website;
        this.role = role;
        this.followers = new HashSet<User>();
        this.following = new HashSet<User>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws UsernameNotUniqueException {
        //TODO: Add check for unique username
        this.username = username;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) throws StringToLongException {
        if(biography.length() > 160) {
            throw new StringToLongException("Biography can not be more then 160 characters long.");
        }

        this.biography = biography;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void addFollower(User user) {
        this.followers.add(user);
    }

    public void removeFollower(User user) {
        this.followers.remove(user);
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void addFollowing(User user) {
        this.following.add(user);
    }

    public void removeFollowing(User user) {
        this.following.remove(user);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
