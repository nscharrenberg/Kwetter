package dtos.users;

import dtos.roles.RoleCleanDto;

public class UserCleanDto {
    private Integer id;
    private String username;
    private String email;
    private String biography;
    private String website;
    private Double longitude;
    private Double latitude;
    private RoleCleanDto role;

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
}
