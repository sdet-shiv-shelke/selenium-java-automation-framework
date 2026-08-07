package io.github.sdetshiv.config;

import java.time.Duration;

public final class ConfigManager {
    private ConfigManager() {
    }

    public static String browser() {
        return value("browser", "BROWSER", "chrome").toLowerCase();
    }

    public static boolean headless() {
        return Boolean.parseBoolean(value("headless", "HEADLESS", "true"));
    }

    public static Duration explicitWait() {
        return Duration.ofSeconds(Long.parseLong(value("waitSeconds", "WAIT_SECONDS", "10")));
    }

    public static String remoteUrl() {
        return value("remoteUrl", "REMOTE_URL", "");
    }

    private static String value(String property, String environmentVariable, String defaultValue) {
        String systemValue = System.getProperty(property);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }

        String environmentValue = System.getenv(environmentVariable);
        return environmentValue == null || environmentValue.isBlank() ? defaultValue : environmentValue;
    }
}
