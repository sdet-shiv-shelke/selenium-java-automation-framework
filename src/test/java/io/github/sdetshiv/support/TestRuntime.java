package io.github.sdetshiv.support;

public final class TestRuntime {
    private static final ThreadLocal<String> BASE_URL = new ThreadLocal<>();

    private TestRuntime() {
    }

    public static void setBaseUrl(String baseUrl) {
        BASE_URL.set(baseUrl);
    }

    public static String baseUrl() {
        String value = BASE_URL.get();
        if (value == null) {
            throw new IllegalStateException("The test application URL has not been set");
        }
        return value;
    }

    public static void clear() {
        BASE_URL.remove();
    }
}
