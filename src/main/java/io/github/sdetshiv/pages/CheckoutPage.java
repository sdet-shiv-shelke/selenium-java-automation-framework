package io.github.sdetshiv.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class CheckoutPage extends BasePage {
    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By REVIEW_ORDER = By.cssSelector("[data-testid='review-order']");
    private static final By ORDER_SUMMARY = By.cssSelector("[data-testid='order-summary']");
    private static final By PLACE_ORDER = By.cssSelector("[data-testid='place-order']");
    private static final By CONFIRMATION = By.cssSelector("[data-testid='confirmation-message']");

    public CheckoutPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public CheckoutPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/checkout"));
        visible(FIRST_NAME);
        return this;
    }

    public CheckoutPage enterCustomer(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        click(REVIEW_ORDER);
        visible(ORDER_SUMMARY);
        return this;
    }

    public CheckoutPage placeOrder() {
        click(PLACE_ORDER);
        wait.until(ExpectedConditions.urlContains("/complete"));
        return this;
    }

    public String confirmationMessage() {
        return text(CONFIRMATION);
    }
}
