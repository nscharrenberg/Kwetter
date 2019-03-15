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

import javax.swing.*;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
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


    @Test
    public void getById() throws InvalidContentException, NotFoundException {
        int id = 324;
        Tweet tweet = new Tweet();
        tweet.setId(id);

        when(tr.getById(id)).thenReturn(tweet);

        tweetService.getById(id);
        verify(tr, atLeastOnce()).getById(id);
    }

    @Test(expected = NotFoundException.class)
    public void getByIdNotFound() throws InvalidContentException, NotFoundException {
        int id = 324;

        when(tr.getById(id)).thenReturn(null);

        tweetService.getById(id);
        verify(tr, never()).getById(id);
    }

    @Test
    public void getByAuthorName() throws InvalidContentException, NotFoundException {
        // Tweet 1
        int tweetId = 324;
        Tweet tweet = new Tweet();
        tweet.setId(tweetId);

        // Tweet 2
        int tweetId2 = 324;
        Tweet tweet2 = new Tweet();
        tweet2.setId(tweetId2);

        // User
        int userId = 4563;
        String username = "thisTestUser";
        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        user.addTweet(tweet);
        user.addTweet(tweet2);

        when(ur.getByUsername(username).getObject()).thenReturn(user);
        when(tr.getByAuthorId(userId)).thenReturn(new ArrayList<>(user.getTweets()));

        tweetService.getByAuthorName(username);
        verify(tr, atLeastOnce()).getByAuthorId(userId);
    }

    @Test
    public void getByAuthorId() throws InvalidContentException, NotFoundException {
        // Tweet 1
        int tweetId = 324;
        Tweet tweet = new Tweet();
        tweet.setId(tweetId);

        // Tweet 2
        int tweetId2 = 324;
        Tweet tweet2 = new Tweet();
        tweet2.setId(tweetId2);

        // Tweet 3
        int tweetId3 = 324;
        Tweet tweet3 = new Tweet();
        tweet3.setId(tweetId3);

        // User
        int userId = 4563;
        String username = "thisTestUser";
        User user = new User();
        user.setId(userId);
        user.setUsername(username);

        user.addTweet(tweet);
        user.addTweet(tweet3);

        when(tr.getByAuthorId(userId)).thenReturn(new ArrayList<>(user.getTweets()));

        tweetService.getByAuthorId(userId);
        verify(tr, atLeastOnce()).getByAuthorId(userId);
    }

    @Test
    public void getByCreatedDate() throws InvalidContentException, NotFoundException, ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date wantedDate = dateFormat.parse("2019-03-09");

        // Tweet 1
        int tweetId = 324;
        Date date = format.parse("2019-03-09 12:56:12");
        Tweet tweet = new Tweet();
        tweet.setId(tweetId);
        tweet.setCreatedAt(date);

        // Tweet 2
        int tweetId2 = 324;
        Date date2 = format.parse("2019-03-09 12:52:12");
        Tweet tweet2 = new Tweet();
        tweet2.setId(tweetId2);
        tweet2.setCreatedAt(date2);

        // Tweet 3
        int tweetId3 = 324;
        Date date3 = format.parse("2019-02-26 15:56:12");
        Tweet tweet3 = new Tweet();
        tweet3.setId(tweetId3);
        tweet3.setCreatedAt(date3);

        // Tweet 4
        int tweetId4 = 324;
        Date date4 = format.parse("2019-03-09 15:56:12");
        Tweet tweet4 = new Tweet();
        tweet4.setId(tweetId4);
        tweet4.setCreatedAt(date4);

        List<Tweet> expected = new ArrayList<>();
        expected.add(tweet);
        expected.add(tweet2);
        expected.add(tweet4);

        when(tr.getByCreatedDate(wantedDate)).thenReturn(expected);

        tweetService.getByCreatedDate(wantedDate);
        verify(tr, atLeastOnce()).getByCreatedDate(wantedDate);
    }

    @Test
    public void createTweet() throws CreationFailedException, NotFoundException, InvalidContentException {
        // User
        int id = 32454;
        User user = new User();
        user.setId(id);

        String text = "This is my tweet";
        Tweet tweet = new Tweet();
        tweet.setMessage(text);
        tweet.setAuthor(user);
        when(ur.getById(id).getObject()).thenReturn(user);
        when(tr.create(tweet)).thenReturn(tweet);

        tweetService.create(tweet);
        verify(tr, atLeastOnce()).create(tweet);
    }

    @Test(expected = InvalidContentException.class)
    public void createTweetWithoutMessage() throws CreationFailedException, NotFoundException, InvalidContentException {
        // User
        int id = 32454;
        User user = new User();
        user.setId(id);

        Tweet tweet = new Tweet();
        tweet.setAuthor(user);

        tweetService.create(tweet);
        verify(tr, never()).create(tweet);
    }

    @Test(expected = NotFoundException.class)
    public void createTweetWithoutAuthor() throws CreationFailedException, NotFoundException, InvalidContentException {
        String text = "This is my tweet";
        Tweet tweet = new Tweet();
        tweet.setMessage(text);

        tweetService.create(tweet);
        verify(tr, never()).create(tweet);
    }

    @Test
    public void updateTweet() throws NotFoundException, InvalidContentException {
        // User
        int userId = 32454;
        User user = new User();
        user.setId(userId);

        int tweetId = 123123;
        String text = "This is my tweet";
        String newText = "This is my updated Tweet";
        Tweet tweet = new Tweet();
        tweet.setId(123123);
        tweet.setMessage(text);
        tweet.setAuthor(user);
        when(tr.getById(tweetId)).thenReturn(tweet);

        tweet.setMessage(newText);

        when(tr.update(tweet)).thenReturn(tweet);

        tweetService.update(tweet);
        verify(tr, atLeastOnce()).update(tweet);
    }

    @Test
    public void deleteTweet() throws NotFoundException, InvalidContentException {
        int id = 234;
        String msg = "this is my tweet";
        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(msg);

        when(tr.getById(id)).thenReturn(tweet);
        when(tr.delete(tweet)).thenReturn(true);

        tweetService.delete(tweet);
        verify(tr, atLeastOnce()).delete(tweet);
    }

    @Test
    public void likeTweet() throws InvalidContentException, NotFoundException, ActionForbiddenException {
        int id = 234;
        String msg = "this is my tweet";
        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(msg);

        int userId = 5456;
        User user = new User();
        user.setId(userId);

        when(tr.getById(id)).thenReturn(tweet);
        when(ur.getById(userId).getObject()).thenReturn(user);
        when(tr.like(tweet, user)).thenReturn(tweet);

        tweetService.like(tweet, user);
        verify(tr, atLeastOnce()).like(tweet, user);
    }

    @Test(expected = ActionForbiddenException.class)
    public void likeTweetYouAlreadyLike() throws InvalidContentException, NotFoundException, ActionForbiddenException {
        int id = 234;
        String msg = "this is my tweet";
        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(msg);

        int userId = 5456;
        User user = new User();
        user.setId(userId);

        tweet.addLike(user);

        when(tr.getById(id)).thenReturn(tweet);
        when(ur.getById(userId).getObject()).thenReturn(user);

        tweetService.like(tweet, user);
        verify(tr, never()).like(tweet, user);
    }

    @Test
    public void unlikeTweet() throws InvalidContentException, NotFoundException, ActionForbiddenException {
        int id = 234;
        String msg = "this is my tweet";
        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(msg);

        int userId = 5456;
        User user = new User();
        user.setId(userId);

        tweet.addLike(user);

        when(tr.getById(id)).thenReturn(tweet);
        when(ur.getById(userId).getObject()).thenReturn(user);
        when(tr.unlike(tweet, user)).thenReturn(tweet);

        tweetService.unlike(tweet, user);
        verify(tr, atLeastOnce()).unlike(tweet, user);
    }

    @Test(expected = ActionForbiddenException.class)
    public void unlikeTweetYouDoNotLike() throws InvalidContentException, NotFoundException, ActionForbiddenException {
        int id = 234;
        String msg = "this is my tweet";
        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(msg);

        int userId = 5456;
        User user = new User();
        user.setId(userId);


        when(tr.getById(id)).thenReturn(tweet);
        when(ur.getById(userId).getObject()).thenReturn(user);

        tweetService.unlike(tweet, user);
        verify(tr, never()).unlike(tweet, user);
    }

    @Test
    public void getMentions() throws InvalidContentException, NotFoundException, CreationFailedException {
        // User 1
        int userId1 = 456456;
        String username1 = "user1";
        User user1 = new User();
        user1.setId(userId1);
        user1.setUsername(username1);

        // User 2
        int userId2 = 1231685;
        String username2 = "user2";
        User user2 = new User();
        user2.setId(userId2);
        user2.setUsername(username2);

        // User 3
        int userId3 = 7865245;
        String username3 = "user3";
        User user3 = new User();
        user3.setId(userId3);
        user3.setUsername(username3);

        // User 4
        int userId4 = 83435;
        String username4 = "user4";
        User user4 = new User();
        user4.setId(userId4);
        user4.setUsername(username4);

        // Tweet
        int id = 443;
        String message = "@user3 @user2 check this awesomething out";
        Tweet tweet = new Tweet();
        tweet.setId(id);
        tweet.setMessage(message);
        tweet.setAuthor(user4);

        when(ur.getById(userId4).getObject()).thenReturn(user4);
        when(ur.getByUsername(username3).getObject()).thenReturn(user3);
        when(ur.getByUsername(username2).getObject()).thenReturn(user2);
        when(tr.create(tweet)).thenReturn(tweet);

        Tweet result = tweetService.create(tweet).getObject();
        assertEquals(tweet.getMentions(), result.getMentions());
        verify(tr, atLeastOnce()).create(tweet);
    }
}
