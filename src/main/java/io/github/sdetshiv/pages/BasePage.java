package io.github.sdetshiv.pages;

import io.github.sdetshiv.config.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final String baseUrl;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, ConfigManager.explicitWait());
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected void type(By locator, String value) {
        WebElement element = visible(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected String text(By locator) {
        return visible(locator).getText();
    }

    protected List<WebElement> all(By locator) {
        return wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(locator, 0));
    }
}
