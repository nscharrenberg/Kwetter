/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package model;

import exception.StringToLongException;
import exception.UsernameNotUniqueException;

import javax.inject.Inject;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class User {
    @Inject
    private int id;

    @Inject
    private String username;

    @Inject
    @Size(min = 1, max = 160)
    private String biography;

    @Inject
    private double locationLongitude;

    @Inject
    private double locationLatitude;

    @Inject
    private String website;

    @Inject
    private Set<User> followers;

    @Inject
    private Set<User> following;

    @Inject
    private Role role;

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
