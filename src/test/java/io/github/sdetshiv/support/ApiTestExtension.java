package io.github.sdetshiv.support;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public final class ApiTestExtension implements BeforeAllCallback, AfterAllCallback {
    private TestApplicationServer server;

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        server = new TestApplicationServer();
        server.start();
        TestRuntime.setBaseUrl(server.baseUrl());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        TestRuntime.clear();
        if (server != null) {
            server.stop();
        }
    }
}
