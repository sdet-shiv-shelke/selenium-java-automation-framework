package io.github.sdetshiv.tests;

import io.github.sdetshiv.pages.InventoryPage;
import io.github.sdetshiv.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginTest extends BaseUiTest {
    @Test
    @Tag("smoke")
    @DisplayName("Valid user can sign in")
    void validUserCanSignIn() {
        InventoryPage inventory = new LoginPage(driver(), baseUrl())
                .open()
                .loginAs("test.user@example.com", "Password123!")
                .waitUntilLoaded();

        assertEquals(3, inventory.productCount());
    }

    @Test
    @Tag("regression")
    @DisplayName("Invalid credentials show a useful error")
    void invalidCredentialsShowError() {
        LoginPage login = new LoginPage(driver(), baseUrl())
                .open()
                .submitInvalidCredentials("invalid.user@example.com", "WrongPassword!");

        assertEquals("Invalid email or password", login.errorMessage());
    }
}
