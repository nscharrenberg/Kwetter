package com.nscharrenberg.kwetter.selenium.tests;

import com.nscharrenberg.kwetter.selenium.setups.LoginPage;
import com.nscharrenberg.kwetter.selenium.utils.SeleniumTestBase;
import com.nscharrenberg.kwetter.selenium.utils.SeleniumTestDirectoryCreator;
import org.junit.Before;
import org.junit.Test;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LoginSeleniumTests extends SeleniumTestBase {
    private LoginPage loginPage;

    public LoginSeleniumTests() {
        super();
    }

    @Before
    public void before() {
        this.loginPage = new LoginPage(webDriver);
    }

    @Test
    public void login_valid_user() throws IOException {
        String loginUrl = WEBSITE_HOST + "auth/login";
        String username = "admin";
        String password = "password123";

        String folder = "login";
        String folderName = SeleniumTestDirectoryCreator.createTestFolder(folder);

        webDriver.get(loginUrl);
        screenshotHelper.saveScreenshot(folder + "/" + folderName + "/login_for_success_test_screenshot.png");
        loginPage.loginCredentials(username, password);
        String nextPage = loginPage.submit();
                screenshotHelper.saveScreenshot(folder + "/" + folderName + "/login_success_screenshot.png");
        assertEquals("@" + username, nextPage);
    }

    @Test
    public void login_invalid_user() throws IOException {
        String loginUrl = WEBSITE_HOST + "auth/login";
        String username = "admin123";
        String password = "wrongpassword";

        String folder = "login_invalid";
        String folderName = SeleniumTestDirectoryCreator.createTestFolder(folder);

        webDriver.get(loginUrl);
        screenshotHelper.saveScreenshot(folder + "/" + folderName + "/login_for_failed_test_screenshot.png");
        loginPage.loginCredentials(username, password);
        String nextPage = loginPage.submit();
                screenshotHelper.saveScreenshot(folder + "/" + folderName + "/login_invalid_screenshot.png");
        assertNotEquals("@" + username, nextPage);
        assertEquals("Wrong username or password", nextPage);
    }
}
