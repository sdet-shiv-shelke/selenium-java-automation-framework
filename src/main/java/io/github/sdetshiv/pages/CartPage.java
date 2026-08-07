package io.github.sdetshiv.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public final class CartPage extends BasePage {
    private static final By HEADING = By.cssSelector("main h1");
    private static final By CART_ITEMS = By.cssSelector("[data-testid='cart-item']");
    private static final By CHECKOUT = By.cssSelector("[data-testid='checkout-link']");

    public CartPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public CartPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/cart"));
        visible(HEADING);
        return this;
    }

    public boolean containsProduct(String productName) {
        return all(CART_ITEMS).stream().anyMatch(item -> item.getText().contains(productName));
    }

    public CheckoutPage checkout() {
        click(CHECKOUT);
        return new CheckoutPage(driver, baseUrl).waitUntilLoaded();
    }
}
