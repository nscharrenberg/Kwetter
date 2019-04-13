package dtos.users;

import domain.User;

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
