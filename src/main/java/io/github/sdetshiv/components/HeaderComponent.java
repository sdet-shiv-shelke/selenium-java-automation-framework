package io.github.sdetshiv.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class HeaderComponent {
    private static final By CART_LINK = By.cssSelector("[data-testid='cart-link']");
    private static final By CART_COUNT = By.cssSelector("[data-testid='cart-count']");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public HeaderComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public int cartCount() {
        String count = wait.until(ExpectedConditions.visibilityOfElementLocated(CART_COUNT)).getText();
        return Integer.parseInt(count);
    }

    public void openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(CART_LINK)).click();
    }
}
