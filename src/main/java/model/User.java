/*
 * Copyright (c) 2019. Noah Scharrenberg
 */

package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import exception.StringToLongException;
import exception.UsernameNotUniqueException;

import javax.inject.Inject;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "user.getAllUsers", query = "SELECT u FROM User u"),
        @NamedQuery(name = "user.getUserById", query = "SELECT u FROM User u WHERE u.id = :id"),
        @NamedQuery(name = "user.getUserByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(length = 160)
    private String biography;

    @Column
    private double locationLongitude;

    @Column
    private double locationLatitude;

    @Column
    private String website;

    @ManyToMany(
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            }
    )
    @JoinTable(
            name = "followers",
            joinColumns = @JoinColumn(name = "follower"),
            inverseJoinColumns = @JoinColumn(name = "following")
    )
    private Set<User> followers;

    @ManyToMany(
            mappedBy = "followers",
            fetch = FetchType.LAZY
    )
    private Set<User> following;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    public User() {
    }

    public User(int id, String username, String password, String biography, double locationLongitude, double locationLatitude, String website, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.biography = biography;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.website = website;
        this.role = role;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
    }

    public User(int id, String username, String password, String biography, double locationLongitude, double locationLatitude, String website) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.biography = biography;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.website = website;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
    }

    public User(String username, String password, String biography, double locationLongitude, double locationLatitude, String website, Role role) {
        this.username = username;
        this.password = password;
        this.biography = biography;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.website = website;
        this.role = role;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
    }

    public User(String username, String password, String biography, double locationLongitude, double locationLatitude, String website) {
        this.username = username;
        this.password = password;
        this.biography = biography;
        this.locationLongitude = locationLongitude;
        this.locationLatitude = locationLatitude;
        this.website = website;
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws UsernameNotUniqueException {
        this.username = username;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) throws StringToLongException {
        this.biography = biography;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    private void addFollower(User user) {
        this.followers.add(user);
    }

    private void removeFollower(User user) {

        this.followers.remove(user);
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void addFollowing(User user) {
        this.following.add(user);
        user.addFollower(this);
    }

    public void removeFollowing(User user) {

        this.following.remove(user);
        user.removeFollower(this);
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
