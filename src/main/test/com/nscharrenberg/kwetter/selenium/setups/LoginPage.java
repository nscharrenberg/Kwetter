package com.nscharrenberg.kwetter.selenium.setups;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private static final String LOGIN_BUTTON_FIELD = "loginBtn";
    private static final int TIMEOUT_SECONDS = 20;

    @FindBy(id = "username")
    private WebElement usernameElement;

    @FindBy(id = "password")
    private WebElement passwordElement;

    @FindBy(id = "loginBtn")
    private WebElement loginBtn;

    private WebDriver webDriver;

    public LoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void loginCredentials(String username, String password) {
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(usernameElement));

        usernameElement.clear();
        usernameElement.sendKeys(username);

        wait.until(ExpectedConditions.elementToBeClickable(passwordElement));
        this.passwordElement.clear();
        this.passwordElement.sendKeys(password);
    }

    public String submit() {
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(loginBtn));
        loginBtn.click();

        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@id,'usernameTag') or (contains(@class,'alert-danger'))]"))).getText();
    }
}
