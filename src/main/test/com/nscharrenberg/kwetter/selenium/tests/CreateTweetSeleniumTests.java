package com.nscharrenberg.kwetter.selenium.tests;

import com.nscharrenberg.kwetter.selenium.setups.CreateTweetPage;
import com.nscharrenberg.kwetter.selenium.setups.LoginPage;
import com.nscharrenberg.kwetter.selenium.utils.SeleniumTestBase;
import com.nscharrenberg.kwetter.selenium.utils.SeleniumTestDirectoryCreator;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CreateTweetSeleniumTests extends SeleniumTestBase {
    private LoginPage loginPage;
    private CreateTweetPage createTweetPage;

    public CreateTweetSeleniumTests() {
        super();
    }

    @Before
    public void before() {
        this.loginPage = new LoginPage(webDriver);
        this.createTweetPage = new CreateTweetPage(webDriver);
    }

    @Test
    public void create_tweet_success() throws IOException {
        String loginUrl = WEBSITE_HOST + "auth/login";
        String username = "admin";
        String password = "password123";
        String message = "This is a selenium test";
        String expectedResult = "Tweet posted";

        String folder = "create_tweet_success";
        String folderName = SeleniumTestDirectoryCreator.createTestFolder(folder);

        webDriver.get(loginUrl);
        screenshotHelper.saveScreenshot(folder + "/" + folderName + "/before_login.png");
        loginPage.loginCredentials(username, password);
        String nextPage = loginPage.submit();
        screenshotHelper.saveScreenshot(folder + "/" + folderName + "/after_login.png");
        assertEquals("@" + username, nextPage);

        createTweetPage.messageText(message);
        screenshotHelper.saveScreenshot(folder + "/" + folderName + "/message_populated.png");
        String createPage = createTweetPage.submit();
        screenshotHelper.saveScreenshot(folder + "/" + folderName + "/message_submitted.png");
        assertEquals(expectedResult, createPage);
    }
}
