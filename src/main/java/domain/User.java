package domain;

import java.util.Set;

public class User {
    private int id;
    private String username;
    private String email;
    private String password;
    private String biography;
    private String website;
    private double longitude;
    private double latitude;
    private Role role;
    private Set<User> followers;
    private Set<User> following;

    public User() {
    }

    public User(int id, String username, String email, String password, String biography, String website, double longitude, double latitude, Role role, Set<User> followers, Set<User> following) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.biography = biography;
        this.website = website;
        this.longitude = longitude;
        this.latitude = latitude;
        this.role = role;
        this.followers = followers;
        this.following = following;
    }

    public User(String username, String email, String password, String biography, String website, double longitude, double latitude, Role role, Set<User> followers, Set<User> following) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.biography = biography;
        this.website = website;
        this.longitude = longitude;
        this.latitude = latitude;
        this.role = role;
        this.followers = followers;
        this.following = following;
    }

    public User(int id, String username, String email, String password, String biography, String website, double longitude, double latitude, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.biography = biography;
        this.website = website;
        this.longitude = longitude;
        this.latitude = latitude;
        this.role = role;
    }

    public User(String username, String email, String password, String biography, String website, double longitude, double latitude, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.biography = biography;
        this.website = website;
        this.longitude = longitude;
        this.latitude = latitude;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<User> getFollowing() {
        return following;
    }

    public void setFollowing(Set<User> following) {
        this.following = following;
    }

    public void addFollowing(User user) {
        this.following.add(user);
        user.addFollower(this);
    }

    private void addFollower(User user) {
        this.followers.add(user);
    }

    public void removeFollowing(User user) {
        this.following.remove(user);
        user.removeFollower(this);
    }

    private void removeFollower(User user) {
        this.followers.remove(user);
    }
}
