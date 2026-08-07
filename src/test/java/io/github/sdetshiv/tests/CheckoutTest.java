package io.github.sdetshiv.tests;

import io.github.sdetshiv.pages.CartPage;
import io.github.sdetshiv.pages.CheckoutPage;
import io.github.sdetshiv.pages.InventoryPage;
import io.github.sdetshiv.pages.LoginPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckoutTest extends BaseUiTest {
    @Test
    @Tag("regression")
    @DisplayName("User can complete the checkout journey")
    void userCanCompleteCheckout() {
        InventoryPage inventory = new LoginPage(driver(), baseUrl())
                .open()
                .loginAs("test.user@example.com", "Password123!")
                .waitUntilLoaded()
                .addProduct("Quality Engineer Backpack");

        assertEquals(1, inventory.cartCount());

        CartPage cart = inventory.openCart();
        assertTrue(cart.containsProduct("Quality Engineer Backpack"));

        CheckoutPage checkout = cart.checkout()
                .enterCustomer("Alex", "Tester", "10001")
                .placeOrder();

        assertTrue(checkout.confirmationMessage().contains("created successfully"));
    }
}
