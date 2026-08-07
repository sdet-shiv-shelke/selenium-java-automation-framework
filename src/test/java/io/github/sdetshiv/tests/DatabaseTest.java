package io.github.sdetshiv.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseTest {
    @Test
    @Tag("regression")
    @DisplayName("Order audit is persisted and can be verified through JDBC")
    void orderAuditCanBeVerified() throws Exception {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:mem:portfolio;DB_CLOSE_DELAY=-1");
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE order_audit (order_id VARCHAR(20), status VARCHAR(20), total DECIMAL(10,2))");
            statement.executeUpdate("INSERT INTO order_audit VALUES ('ORD-2001', 'CONFIRMED', 79.98)");

            try (ResultSet result = statement.executeQuery("SELECT status, total FROM order_audit WHERE order_id = 'ORD-2001'")) {
                result.next();
                assertEquals("CONFIRMED", result.getString("status"));
                assertEquals("79.98", result.getBigDecimal("total").toPlainString());
            }
        }
    }
}
