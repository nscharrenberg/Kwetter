package com.nscharrenberg.kwetter.selenium.setups;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateTweetPage {
    private static final String MESSAGE_FIELD = "message";
    private static final String SUBMIT_BUTTON = "submit-tweet";
    private static final int TIMEOUT_SECONDS = 20;

    @FindBy(id = MESSAGE_FIELD)
    private WebElement messageElement;

    @FindBy(id = SUBMIT_BUTTON)
    private WebElement submitBtn;

    private WebDriver webDriver;

    public CreateTweetPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void messageText(String message) {
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(messageElement));

        messageElement.clear();
        messageElement.sendKeys(message);
    }

    public String submit() {
        WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT_SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(submitBtn));
        submitBtn.click();

        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(@class,'alert-danger') or (contains(@class,'alert-success'))]"))).getText();
    }
}
