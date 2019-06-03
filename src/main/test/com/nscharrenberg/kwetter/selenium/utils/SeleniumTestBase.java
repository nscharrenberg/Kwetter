package com.nscharrenberg.kwetter.selenium.utils;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;

public class SeleniumTestBase {
    protected static final String WEBSITE_HOST = "http://localhost:4200/";

    protected WebDriver webDriver;
    protected ScreenshotHelper screenshotHelper;

    public SeleniumTestBase() {
        super();
    }

    @Before
    public void setup() {
        String projectLocation = System.getProperty("user.dir");
        System.setProperty("webdriver.chrome.driver", projectLocation + "\\selenium\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");

        webDriver = new ChromeDriver(options);
        screenshotHelper = new ScreenshotHelper(webDriver);

        // Maximize Window
        webDriver.manage().window().maximize();
    }

    @After
    public void close() throws IOException {
        webDriver.close();
    }

    public static String HOST() {
        return WEBSITE_HOST;
    }
}
