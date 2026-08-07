package io.github.sdetshiv.tests;

import io.github.sdetshiv.support.ApiTestExtension;
import io.github.sdetshiv.support.TestRuntime;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(ApiTestExtension.class)
class ProductsApiTest {
    private static String baseUrl;

    @BeforeAll
    static void configureApi() {
        baseUrl = TestRuntime.baseUrl();
    }

    @Test
    @Tag("smoke")
    @DisplayName("GET products returns the catalog")
    void getProductsReturnsCatalog() {
        given()
                .baseUri(baseUrl)
        .when()
                .get("/api/products")
        .then()
                .statusCode(200)
                .contentType("application/json")
                .body("products", hasSize(3))
                .body("products[0].id", greaterThan(0))
                .body("products[0].name", equalTo("Quality Engineer Backpack"));
    }

    @Test
    @Tag("regression")
    @DisplayName("POST orders creates an order")
    void createOrderReturnsConfirmation() {
        given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .body("{\"productIds\":[1,2]}")
        .when()
                .post("/api/orders")
        .then()
                .statusCode(201)
                .body("orderId", equalTo("ORD-2001"))
                .body("status", equalTo("confirmed"));
    }

    @Test
    @Tag("regression")
    @DisplayName("POST orders rejects an empty order")
    void emptyOrderIsRejected() {
        given()
                .baseUri(baseUrl)
                .contentType("application/json")
                .body("{\"productIds\":[]}")
        .when()
                .post("/api/orders")
        .then()
                .statusCode(400)
                .body("error", equalTo("productIds must contain at least one item"));
    }
}
