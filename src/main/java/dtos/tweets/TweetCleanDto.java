package dtos.tweets;

import dtos.users.UserCleanDto;

import java.util.Date;

public class TweetCleanDto {
    private Integer id;
    private String message;
    private Date createdAt;
    private UserCleanDto author;
}
