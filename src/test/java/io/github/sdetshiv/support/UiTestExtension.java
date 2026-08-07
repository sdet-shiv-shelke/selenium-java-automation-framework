package io.github.sdetshiv.support;

import io.github.sdetshiv.driver.DriverManager;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class UiTestExtension implements BeforeAllCallback, AfterAllCallback,
        BeforeEachCallback, AfterTestExecutionCallback, AfterEachCallback {
    private TestApplicationServer server;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        server = new TestApplicationServer();
        server.start();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        TestRuntime.setBaseUrl(server.baseUrl());
        DriverManager.createDriver();
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws IOException {
        if (context.getExecutionException().isPresent() && DriverManager.hasDriver()) {
            byte[] image = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
            Path directory = Path.of("target", "failure-screenshots");
            Files.createDirectories(directory);
            String name = context.getDisplayName().replaceAll("[^a-zA-Z0-9._-]", "_");
            Files.write(directory.resolve(name + ".png"), image);
        }
    }

    @Override
    public void afterEach(ExtensionContext context) {
        DriverManager.quitDriver();
        TestRuntime.clear();
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if (server != null) {
            server.stop();
        }
    }
}
