package com.nscharrenberg.kwetter.selenium.setups;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final String LOGIN_BUTTON_FIELD = "loginBtn";
    private static final int TIMEOUT_SECONDS = 20;

    public String login(WebDriver driver, String username, String password) {
        WebDriverWait wait = new WebDriverWait(driver, TIMEOUT_SECONDS);

        // Set Username Field
        WebElement usernameElement = wait.until(ExpectedConditions.elementToBeClickable(By.id(USERNAME_FIELD)));
        usernameElement.sendKeys(username);

        WebElement passwordElement = wait.until(ExpectedConditions.elementToBeClickable(By.id(PASSWORD_FIELD)));
        passwordElement.sendKeys(password);

        WebElement loginButtonElement = wait.until(ExpectedConditions.elementToBeClickable(By.id(LOGIN_BUTTON_FIELD)));
        loginButtonElement.click();

        WebElement loginPage =wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'usernameTag') or (contains(@class,'alert-danger'))]")));

        return loginPage.getText();
    }
}
