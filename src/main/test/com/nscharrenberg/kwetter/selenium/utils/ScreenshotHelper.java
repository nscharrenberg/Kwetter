package com.nscharrenberg.kwetter.selenium.utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ScreenshotHelper {
    private WebDriver webDriver;

    public ScreenshotHelper(WebDriver webDriver) {
        super();
        this.webDriver = webDriver;
    }

    public void saveScreenshot(String screenshotFileName) throws IOException {
        File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(screenshot, new File("tests_results/" + screenshotFileName));
    }
}
