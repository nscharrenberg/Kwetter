package dtos.users;

import dtos.roles.RoleCleanDto;
import dtos.tweets.TweetCleanDto;

import java.util.Set;

public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String avatar;
    private String biography;
    private String website;
    private Double longitude;
    private Double latitude;
    private RoleCleanDto role;
    private Set<UserCleanDto> followers;
    private Set<UserCleanDto> following;
    private Set<TweetCleanDto> tweets;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public RoleCleanDto getRole() {
        return role;
    }

    public void setRole(RoleCleanDto role) {
        this.role = role;
    }

    public Set<UserCleanDto> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<UserCleanDto> followers) {
        this.followers = followers;
    }

    public Set<UserCleanDto> getFollowing() {
        return following;
    }

    public void setFollowing(Set<UserCleanDto> following) {
        this.following = following;
    }

    public Set<TweetCleanDto> getTweets() {
        return tweets;
    }

    public void setTweets(Set<TweetCleanDto> tweets) {
        this.tweets = tweets;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
