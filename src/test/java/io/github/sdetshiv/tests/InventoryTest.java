package io.github.sdetshiv.tests;

import io.github.sdetshiv.pages.InventoryPage;
import io.github.sdetshiv.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InventoryTest extends BaseUiTest {
    @Test
    @Tag("smoke")
    @DisplayName("User can add multiple products to the cart")
    void userCanAddMultipleProducts() {
        InventoryPage inventory = new LoginPage(driver(), baseUrl())
                .open()
                .loginAs("test.user@example.com", "Password123!")
                .waitUntilLoaded()
                .addProduct("Quality Engineer Backpack")
                .addProduct("Automation Testing Toolkit");

        assertEquals(2, inventory.cartCount());
    }
}
