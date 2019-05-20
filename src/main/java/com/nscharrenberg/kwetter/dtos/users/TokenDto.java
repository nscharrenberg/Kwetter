package com.nscharrenberg.kwetter.dtos.users;

public class TokenDto {
    private String token;
    private UserCleanDto user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserCleanDto getUser() {
        return user;
    }

    public void setUser(UserCleanDto user) {
        this.user = user;
    }
}
