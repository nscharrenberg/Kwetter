package domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(name = "user.getAllUsers", query = "SELECT u FROM User u"),
        @NamedQuery(name = "user.getUserById", query = "SELECT u FROM User u WHERE u.id = :id"),
        @NamedQuery(name = "user.getUserByUsername", query = "SELECT u FROM User u WHERE u.username = :username")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(length = 160)
    private String biography;

    @Column
    private String website;
    private double longitude;
    private double latitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

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

    public User() {
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
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
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
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
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
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
