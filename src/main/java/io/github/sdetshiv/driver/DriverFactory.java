package io.github.sdetshiv.driver;

import io.github.sdetshiv.config.ConfigManager;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

final class DriverFactory {
    private DriverFactory() {
    }

    static WebDriver create() {
        String browser = ConfigManager.browser();
        MutableCapabilities options = optionsFor(browser);
        WebDriver driver = ConfigManager.remoteUrl().isBlank()
                ? localDriver(browser, options)
                : remoteDriver(options);

        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));
        return driver;
    }

    private static MutableCapabilities optionsFor(String browser) {
        return switch (browser) {
            case "chrome" -> chromeOptions();
            case "firefox" -> firefoxOptions();
            case "edge" -> edgeOptions();
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private static ChromeOptions chromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1440,1000", "--disable-dev-shm-usage", "--no-sandbox");
        if (ConfigManager.headless()) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    private static FirefoxOptions firefoxOptions() {
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1440", "--height=1000");
        if (ConfigManager.headless()) {
            options.addArguments("--headless");
        }
        return options;
    }

    private static EdgeOptions edgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--window-size=1440,1000", "--disable-dev-shm-usage", "--no-sandbox");
        if (ConfigManager.headless()) {
            options.addArguments("--headless=new");
        }
        return options;
    }

    private static WebDriver localDriver(String browser, MutableCapabilities options) {
        return switch (browser) {
            case "chrome" -> new ChromeDriver((ChromeOptions) options);
            case "firefox" -> new FirefoxDriver((FirefoxOptions) options);
            case "edge" -> new EdgeDriver((EdgeOptions) options);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    private static WebDriver remoteDriver(MutableCapabilities options) {
        try {
            return new RemoteWebDriver(URI.create(ConfigManager.remoteUrl()).toURL(), options);
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException("REMOTE_URL is not a valid URL", exception);
        }
    }
}
