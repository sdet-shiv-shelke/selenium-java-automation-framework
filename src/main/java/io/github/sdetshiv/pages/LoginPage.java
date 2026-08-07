package io.github.sdetshiv.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class LoginPage extends BasePage {
    private static final By EMAIL = By.id("username");
    private static final By PASSWORD = By.id("password");
    private static final By SIGN_IN = By.cssSelector("[data-testid='login-button']");
    private static final By ERROR = By.id("login-error");

    public LoginPage(WebDriver driver, String baseUrl) {
        super(driver, baseUrl);
    }

    public LoginPage open() {
        driver.get(baseUrl + "/login");
        visible(EMAIL);
        return this;
    }

    public InventoryPage loginAs(String email, String password) {
        type(EMAIL, email);
        type(PASSWORD, password);
        click(SIGN_IN);
        return new InventoryPage(driver, baseUrl);
    }

    public LoginPage submitInvalidCredentials(String email, String password) {
        type(EMAIL, email);
        type(PASSWORD, password);
        click(SIGN_IN);
        return this;
    }

    public String errorMessage() {
        return text(ERROR);
    }
}
