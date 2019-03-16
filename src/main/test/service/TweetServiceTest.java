package service;

import domain.Tweet;
import domain.User;
import exceptions.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import repository.interfaces.TweetRepository;
import repository.interfaces.UserRepository;
import responses.HttpStatusCodes;
import responses.ObjectResponse;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class TweetServiceTest {
    @InjectMocks
    private TweetService tweetService;

    @Mock
    private TweetRepository tr;

    @Mock
    private UserService ur;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void tweetServiceTest() {
        assertNotNull(tweetService);
    }

    private User user;
    private User user2;
    private User user3;

    @Before
    public void init() {
        user = new User();
        user.setId(1);
        user.setUsername("testUser1");
        user.setEmail("testUser1@mail.com");
        user.setPassword("Password123");

        user2 = new User();
        user2.setId(2);
        user2.setUsername("testUser2");
        user2.setEmail("testUser2@mail.com");
        user2.setPassword("Password123");

        user3 = new User();
        user3.setId(3);
        user3.setUsername("testUser3");
        user3.setEmail("testUser3@mail.com");
        user3.setPassword("Password123");
    }

    private List<Tweet> tweetList() {
        // Arrange
        List<Tweet> list = new ArrayList<>();

        for(int i = 0; i < 5; i++) {
            Tweet t = new Tweet();
            t.setId(list.size() + 1);
            t.setMessage("This is a message by testUser1 - " + i);
            t.setAuthor(user);
            t.setCreatedAt(new Date());
            list.add(t);
        }

        for(int i = 0; i < 2; i++) {
            Tweet t = new Tweet();
            t.setId(list.size() + 1);
            t.setMessage("This is a message by testUser2 - " + i);
            t.setAuthor(user2);
            t.setCreatedAt(new Date());
            list.add(t);
        }

        Tweet t = new Tweet();
        t.setId(list.size() + 1);
        t.setMessage("@testUser2 I'm mentioning you");
        t.setAuthor(user);
        t.setCreatedAt(new Date());
        t.addMention(user2);
        list.add(t);

        return list;
    }

    /*
     * Get All Users Test
     */
    @Test
    public void all_StatusCodeOk() {
        // Arrange
        List<Tweet> list = tweetList();
        when(tr.all()).thenReturn(list);

        // Act
        ObjectResponse<List<Tweet>> response = tweetService.all();

        // Assert
        verify(tr, atLeastOnce()).all();
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(list, response.getObject());
        assertEquals(list.size(), response.getObject().size());
        assertEquals(8, response.getObject().size());
    }

    /*
     * Get Tweet By Id Tests
     */
    @Test
    public void getById_ExistingId_StatusCodeOk() {
        // Arange
        int id = 6;
        String message = "This is a message";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        when(tr.getById(id)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.getById(id);

        // Assert
        verify(tr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(tweet, response.getObject());
    }

    @Test
    public void getById_IdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 0;
        String message = "This is a message";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        // Act
        ObjectResponse<Tweet> response = tweetService.getById(id);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void getById_TweetDoesNotExist_StatusCodeOk() {
        // Arange
        int id = 6;
        String message = "This is a message";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        when(tr.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<Tweet> response = tweetService.getById(id);

        // Assert
        verify(tr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Get Tweet By Username Tests
     */
    @Test
    public void getByAuthorName_ExistingUserName_StatusCodeOk() {
        // Arange

        when(ur.getByUsername(user.getUsername())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " found", user));
        when(ur.getById(user.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " found", user));
        when(tr.getByAuthorId(user.getId())).thenReturn(new ArrayList<>(user.getTweets()));

        // Act
        ObjectResponse<List<Tweet>> response = tweetService.getByAuthorName(user.getUsername());

        // Assert
        verify(ur, atLeastOnce()).getByUsername(user.getUsername());
        verify(ur, atLeastOnce()).getById(user.getId());
        verify(tr, atLeastOnce()).getByAuthorId(user.getId());
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(new ArrayList<>(user.getTweets()), response.getObject());
    }

    @Test
    public void getByAuthorName_UsernameEmpty_StatusCodeNotAcceptable() {
        // Arange
        String username = "";

        // Act
        ObjectResponse<List<Tweet>> response = tweetService.getByAuthorName(username);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void getByAuthorName_UsernameDoesNotExist_StatusCodeNotFound() {
        // Arange
        String username = "iDoNotExist";

        when(ur.getByUsername(username)).thenReturn(new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "User not found"));

        // Act
        ObjectResponse<List<Tweet>> response = tweetService.getByAuthorName(username);

        // Assert
        verify(ur, atLeastOnce()).getByUsername(username);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Get Tweet By User Id Tests
     */
    @Test
    public void getByAuthorId_ExistingId_StatusCodeOk() {
        // Arange
        when(ur.getById(user.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " found", user));
        when(tr.getByAuthorId(user.getId())).thenReturn(new ArrayList<>(user.getTweets()));

        // Act
        ObjectResponse<List<Tweet>> response = tweetService.getByAuthorId(user.getId());

        // Assert
        verify(ur, atLeastOnce()).getById(user.getId());
        verify(tr, atLeastOnce()).getByAuthorId(user.getId());
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(new ArrayList<>(user.getTweets()), response.getObject());
    }

    @Test
    public void getByAuthorId_IdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 0;

        // Act
        ObjectResponse<List<Tweet>> response = tweetService.getByAuthorId(id);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void getByAuthorId_IdDoesNotExist_StatusCodeNotFound() {
        // Arange
        int id = 156;

        when(ur.getById(id)).thenReturn(new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "User not found"));

        // Act
        ObjectResponse<List<Tweet>> response = tweetService.getByAuthorId(id);

        // Assert
        verify(ur, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Create Tweet Tests
     */
    @Test
    public void create_newWithProperItems_StatusCodeOk() {
        // Arange
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setMessage(message);
        tweet.setAuthor(author);

        when(ur.getById(user.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " found", user));
        when(tr.create(tweet)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.create(tweet);

        // Assert
        verify(ur, atLeastOnce()).getById(user.getId());
        verify(tr, atLeastOnce()).create(tweet);
        assertEquals(HttpStatusCodes.CREATED, response.getCode());
        assertEquals(tweet, response.getObject());
    }

    @Test
    public void create_EmptyMessage_StatusCodeNotAcceptable() {
        // Arange
        String message = "";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setMessage(message);
        tweet.setAuthor(author);

        // Act
        ObjectResponse<Tweet> response = tweetService.create(tweet);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void create_EmptyAuthor_StatusCodeNotAcceptable() {
        // Arange
        String message = "This is a message";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setMessage(message);

        // Act
        ObjectResponse<Tweet> response = tweetService.create(tweet);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void create_AuthorDoesNotExist_StatusCodeNotFound() {
        // Arange
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setMessage(message);
        tweet.setAuthor(author);

        when(ur.getById(user.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "User not found"));

        // Act
        ObjectResponse<Tweet> response = tweetService.create(tweet);

        // Assert
        verify(ur, atLeastOnce()).getById(user.getId());
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void create_newWithProperItemsAndOneMentions_StatusCodeOk() {
        // Arange
        String message = "@testUser2 This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setMessage(message);
        tweet.setAuthor(author);

        Set<User> expectedMentions = new HashSet<>();
        expectedMentions.add(user2);

        when(ur.getById(user.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " found", user));
        when(ur.getByUsername(user2.getUsername())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user2.getUsername() + " found", user2));
        when(tr.create(tweet)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.create(tweet);

        // Assert
        verify(ur, atLeastOnce()).getById(user.getId());
        verify(ur, atLeastOnce()).getByUsername(user2.getUsername());
        verify(tr, atLeastOnce()).create(tweet);
        assertEquals(HttpStatusCodes.CREATED, response.getCode());
        assertEquals(tweet, response.getObject());
        assertEquals(expectedMentions, response.getObject().getMentions());
    }

    @Test
    public void create_newWithProperItemsAndMultipleMentions_StatusCodeOk() {
        // Arange
        String message = "@testUser3 @testUser2 This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setMessage(message);
        tweet.setAuthor(author);

        Set<User> expectedMentions = new HashSet<>();
        expectedMentions.add(user2);
        expectedMentions.add(user3);

        when(ur.getById(user.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " found", user));
        when(ur.getByUsername(user2.getUsername())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user2.getUsername() + " found", user2));
        when(ur.getByUsername(user3.getUsername())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user3.getUsername() + " found", user3));
        when(tr.create(tweet)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.create(tweet);

        // Assert
        verify(ur, atLeastOnce()).getById(user.getId());
        verify(ur, atLeastOnce()).getByUsername(user2.getUsername());
        verify(ur, atLeastOnce()).getByUsername(user3.getUsername());
        verify(tr, atLeastOnce()).create(tweet);
        assertEquals(HttpStatusCodes.CREATED, response.getCode());
        assertEquals(tweet, response.getObject());
        assertEquals(expectedMentions, response.getObject().getMentions());
    }

    @Test
    public void create_newWithProperItemsAndMultipleMentionsNotAllExisting_StatusCodeOk() {
        // Arange
        String message = "@testUser3 @testUser2 @iDoNotExist This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setMessage(message);
        tweet.setAuthor(author);

        Set<User> expectedMentions = new HashSet<>();
        expectedMentions.add(user2);
        expectedMentions.add(user3);

        when(ur.getById(user.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user.getUsername() + " found", user));
        when(ur.getByUsername(user2.getUsername())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user2.getUsername() + " found", user2));
        when(ur.getByUsername(user3.getUsername())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user3.getUsername() + " found", user3));
        when(ur.getByUsername("iDoNotExist")).thenReturn(new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "User not found"));
        when(tr.create(tweet)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.create(tweet);

        // Assert
        verify(ur, atLeastOnce()).getById(user.getId());
        verify(ur, atLeastOnce()).getByUsername(user2.getUsername());
        verify(ur, atLeastOnce()).getByUsername(user3.getUsername());
        verify(ur, atLeastOnce()).getByUsername("iDoNotExist");
        verify(tr, atLeastOnce()).create(tweet);
        assertEquals(HttpStatusCodes.CREATED, response.getCode());
        assertEquals(tweet, response.getObject());
        assertEquals(expectedMentions, response.getObject().getMentions());
    }

    /*
     * update Tweets Tests
     */
    @Test
    public void update_newWithProperItems_StatusCodeOk() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        when(tr.getById(id)).thenReturn(tweet);
        when(tr.update(tweet)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.update(tweet);

        // Assert
        verify(tr, atLeastOnce()).getById(id);
        verify(tr, atLeastOnce()).update(tweet);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(tweet, response.getObject());
    }

    @Test
    public void update_EmptyMessage_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String message = "";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        // Act
        ObjectResponse<Tweet> response = tweetService.update(tweet);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_EmptyAuthor_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        Tweet updated = new Tweet();
        updated.setId(id);
        updated.setMessage(message);

        when(tr.getById(id)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.update(updated);

        // Assert
        verify(tr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_changeAuthor_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;
        User updatedAuthor = user2;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        Tweet updated=  new Tweet();
        updated.setId(id);
        updated.setMessage(message);
        updated.setAuthor(updatedAuthor);

        when(tr.getById(id)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.update(updated);

        // Assert
        verify(tr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void update_newWithProperItemsAndOneMentions_StatusCodeOk() {
        // Arange
        int id = 6;
        String message = "@testUser2 This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        Set<User> expectedMentions = new HashSet<>();
        expectedMentions.add(user2);

        when(tr.getById(id)).thenReturn(tweet);
        when(ur.getByUsername(user2.getUsername())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user2.getUsername() + " found", user2));
        when(tr.update(tweet)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.update(tweet);

        // Assert
        verify(tr, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getByUsername(user2.getUsername());
        verify(tr, atLeastOnce()).update(tweet);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(tweet, response.getObject());
        assertEquals(expectedMentions, response.getObject().getMentions());
    }

    @Test
    public void update_newWithProperItemsAndMultipleMentions_StatusCodeOk() {
        // Arange
        int id = 6;
        String message = "@testUser3 @testUser2 This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        Set<User> expectedMentions = new HashSet<>();
        expectedMentions.add(user2);
        expectedMentions.add(user3);

        when(tr.getById(id)).thenReturn(tweet);
        when(ur.getByUsername(user2.getUsername())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user2.getUsername() + " found", user2));
        when(ur.getByUsername(user3.getUsername())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user3.getUsername() + " found", user3));
        when(tr.update(tweet)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.update(tweet);

        // Assert
        verify(tr, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getByUsername(user2.getUsername());
        verify(ur, atLeastOnce()).getByUsername(user3.getUsername());
        verify(tr, atLeastOnce()).update(tweet);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(tweet, response.getObject());
        assertEquals(expectedMentions, response.getObject().getMentions());
    }

    @Test
    public void update_newWithProperItemsAndMultipleMentionsNotAllExisting_StatusCodeOk() {
        // Arange
        int id = 6;
        String message = "@testUser3 @testUser2 @iDoNotExist This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        Set<User> expectedMentions = new HashSet<>();
        expectedMentions.add(user2);
        expectedMentions.add(user3);

        when(tr.getById(id)).thenReturn(tweet);
        when(ur.getByUsername(user2.getUsername())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user2.getUsername() + " found", user2));
        when(ur.getByUsername(user3.getUsername())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + user3.getUsername() + " found", user3));
        when(ur.getByUsername("iDoNotExist")).thenReturn(new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "User not found"));
        when(tr.update(tweet)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.update(tweet);

        // Assert
        verify(tr, atLeastOnce()).getById(id);
        verify(ur, atLeastOnce()).getByUsername(user2.getUsername());
        verify(ur, atLeastOnce()).getByUsername(user3.getUsername());
        verify(ur, atLeastOnce()).getByUsername("iDoNotExist");
        verify(tr, atLeastOnce()).update(tweet);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(tweet, response.getObject());
        assertEquals(expectedMentions, response.getObject().getMentions());
    }


    /*
     * Delete Tweet Tests
     */
    @Test
    public void delete_ExistingId_StatusCodeOk() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);
        when(tr.getById(id)).thenReturn(tweet);
        when(tr.delete(tweet)).thenReturn(true);

        // Act
        ObjectResponse<Tweet> response = tweetService.delete(tweet);

        // Assert
        verify(tr, atLeastOnce()).getById(id);
        verify(tr, atLeastOnce()).delete(tweet);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void delete_IdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 0;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        // Act
        ObjectResponse<Tweet> response = tweetService.delete(tweet);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void delete_TweetDoesNotExist_StatusCodeNotFound() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);
        when(tr.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<Tweet> response = tweetService.delete(tweet);

        // Assert
        verify(tr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Like Tweet Tests
     */
    @Test
    public void like_withExistingUserAndtweet_StatusCodeOk() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        User liker = user2;

        Tweet expected = new Tweet();
        expected.setId(id);
        expected.setMessage(message);
        expected.setAuthor(author);
        expected.addLike(liker);

        when(ur.getById(liker.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + liker.getUsername() + " found", liker));
        when(tr.getById(id)).thenReturn(tweet);
        when(tr.like(tweet, liker)).thenReturn(expected);

        // Act
        ObjectResponse<Tweet> response = tweetService.like(tweet, liker);

        // Assert
        verify(ur, atLeastOnce()).getById(liker.getId());
        verify(tr, atLeastOnce()).getById(id);
        verify(tr, atLeastOnce()).like(tweet, liker);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(tweet.getId(), response.getObject().getId());
        assertTrue(response.getObject().getLikes().contains(liker));
    }

    @Test
    public void like_UserIdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        User liker = user2;

        liker.setId(0);

        // Act
        ObjectResponse<Tweet> response = tweetService.like(tweet, liker);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void like_TweetIdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 0;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        User liker = user2;

        // Act
        ObjectResponse<Tweet> response = tweetService.like(tweet, liker);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
}

    @Test
    public void like_UserDoesNotExist_StatusCodeNotFound() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        User liker = user2;

        when(ur.getById(liker.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "User not found"));

        // Act
        ObjectResponse<Tweet> response = tweetService.like(tweet, liker);

        // Assert
        verify(ur, atLeastOnce()).getById(liker.getId());
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void like_TweetDoesNotExist_StatusCodeNotFound() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        User liker = user2;

        when(ur.getById(liker.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + liker.getUsername() + " found", liker));
        when(tr.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<Tweet> response = tweetService.like(tweet, liker);

        // Assert
        verify(ur, atLeastOnce()).getById(liker.getId());
        verify(tr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void like_UserAlreadyLikesTweet_StatusCodeForbidden() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        User liker = user2;

        tweet.addLike(liker);

        when(ur.getById(liker.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + liker.getUsername() + " found", liker));
        when(tr.getById(id)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.like(tweet, liker);

        // Assert
        verify(ur, atLeastOnce()).getById(liker.getId());
        verify(tr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.FORBIDDEN, response.getCode());
        assertNull(response.getObject());
    }

    /*
     * Unlike Tweet Tests
     */
    @Test
    public void unlike_withExistingUserAndtweet_StatusCodeOk() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;
        User liker = user2;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);
        tweet.addLike(liker);

        Tweet expected = new Tweet();
        expected.setId(id);
        expected.setMessage(message);
        expected.setAuthor(author);

        when(ur.getById(liker.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + liker.getUsername() + " found", liker));
        when(tr.getById(id)).thenReturn(tweet);
        when(tr.unlike(tweet, liker)).thenReturn(expected);

        // Act
        ObjectResponse<Tweet> response = tweetService.unlike(tweet, liker);

        // Assert
        verify(ur, atLeastOnce()).getById(liker.getId());
        verify(tr, atLeastOnce()).getById(id);
        verify(tr, atLeastOnce()).unlike(tweet, liker);
        assertEquals(HttpStatusCodes.OK, response.getCode());
        assertEquals(tweet.getId(), response.getObject().getId());
        assertFalse(response.getObject().getLikes().contains(liker));
    }

    @Test
    public void unlike_UserIdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;
        User liker = user2;
        liker.setId(0);

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);
        tweet.addLike(liker);

        // Act
        ObjectResponse<Tweet> response = tweetService.unlike(tweet, liker);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void unlike_TweetIdNull_StatusCodeNotAcceptable() {
        // Arange
        int id = 0;
        String message = "This is a message of a tweet";
        User author = user;
        User liker = user2;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);
        tweet.addLike(liker);

        // Act
        ObjectResponse<Tweet> response = tweetService.unlike(tweet, liker);

        // Assert
        assertEquals(HttpStatusCodes.NOT_ACCEPTABLE, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void unlike_UserDoesNotExist_StatusCodeNotFound() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;
        User liker = user2;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);
        tweet.addLike(liker);

        when(ur.getById(liker.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.NOT_FOUND, "User not found"));

        // Act
        ObjectResponse<Tweet> response = tweetService.unlike(tweet, liker);

        // Assert
        verify(ur, atLeastOnce()).getById(liker.getId());
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void unlike_TweetDoesNotExist_StatusCodeNotFound() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;
        User liker = user2;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);
        tweet.addLike(liker);

        when(ur.getById(liker.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + liker.getUsername() + " found", liker));
        when(tr.getById(id)).thenReturn(null);

        // Act
        ObjectResponse<Tweet> response = tweetService.unlike(tweet, liker);

        // Assert
        verify(ur, atLeastOnce()).getById(liker.getId());
        verify(tr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.NOT_FOUND, response.getCode());
        assertNull(response.getObject());
    }

    @Test
    public void unlike_UserDoesNotLikeTweet_StatusCodeForbidden() {
        // Arange
        int id = 6;
        String message = "This is a message of a tweet";
        User author = user;

        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(author);

        User liker = user2;

        when(ur.getById(liker.getId())).thenReturn(new ObjectResponse<>(HttpStatusCodes.OK, "User with username: " + liker.getUsername() + " found", liker));
        when(tr.getById(id)).thenReturn(tweet);

        // Act
        ObjectResponse<Tweet> response = tweetService.unlike(tweet, liker);

        // Assert
        verify(ur, atLeastOnce()).getById(liker.getId());
        verify(tr, atLeastOnce()).getById(id);
        assertEquals(HttpStatusCodes.FORBIDDEN, response.getCode());
        assertNull(response.getObject());
    }
}
