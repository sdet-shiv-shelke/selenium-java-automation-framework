package io.github.sdetshiv.tests;

import io.github.sdetshiv.driver.DriverManager;
import io.github.sdetshiv.support.TestRuntime;
import io.github.sdetshiv.support.UiTestExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;

@ExtendWith(UiTestExtension.class)
abstract class BaseUiTest {
    protected WebDriver driver() {
        return DriverManager.getDriver();
    }

    protected String baseUrl() {
        return TestRuntime.baseUrl();
    }
}
