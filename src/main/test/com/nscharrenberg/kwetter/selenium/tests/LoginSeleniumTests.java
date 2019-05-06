package com.nscharrenberg.kwetter.selenium.tests;

import com.nscharrenberg.kwetter.selenium.setups.LoginPage;
import com.nscharrenberg.kwetter.selenium.utils.SeleniumTestBase;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class LoginSeleniumTests extends SeleniumTestBase {
    private LoginPage loginPage = new LoginPage();

    @Test
    public void login_valid_user() throws IOException {
        String loginUrl = WEBSITE_HOST + "auth/login";
        String username = "admin";
        String password = "password123";

        webDriver.get(loginUrl);
        screenshotHelper.saveScreenshot("login_for_success_test_screenshot.png");
        String nextPage = loginPage.login(webDriver, username, password);
        screenshotHelper.saveScreenshot("login_success_screenshot.png");
        assertEquals("@" + username, nextPage);
    }

    @Test
    public void login_invalid_user() throws IOException {
        String loginUrl = WEBSITE_HOST + "auth/login";
        String username = "admin123";
        String password = "wrongpassword";

        webDriver.get(loginUrl);
        screenshotHelper.saveScreenshot("login_for_failed_test_screenshot.png");
        String nextPage = loginPage.login(webDriver, username, password);
        screenshotHelper.saveScreenshot("login_invalid_screenshot.png");
        assertNotEquals("@" + username, nextPage);
        assertEquals("Wrong username or password", nextPage);
    }
}
