package io.github.sdetshiv.pages;

import io.github.sdetshiv.components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public final class InventoryPage extends BasePage {
    private static final By HEADING = By.cssSelector("main h1");
    private static final By PRODUCT_CARDS = By.cssSelector("[data-testid='product-card']");

    private final HeaderComponent header;

    public InventoryPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
        this.header = new HeaderComponent(driver);
    }

    public InventoryPage waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/inventory"));
        visible(HEADING);
        return this;
    }

    public int productCount() {
        return all(PRODUCT_CARDS).size();
    }

    public InventoryPage addProduct(String productName) {
        WebElement card = productCards().stream()
                .filter(element -> element.findElement(By.tagName("h2")).getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Product not found: " + productName));
        card.findElement(By.cssSelector("[data-testid='add-to-cart']")).click();
        return this;
    }

    public int cartCount() {
        return header.cartCount();
    }

    public CartPage openCart() {
        header.openCart();
        return new CartPage(driver, baseUrl).waitUntilLoaded();
    }

    private List<WebElement> productCards() {
        return all(PRODUCT_CARDS);
    }
}
