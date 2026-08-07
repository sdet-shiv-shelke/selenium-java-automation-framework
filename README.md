# Selenium Java Automation Framework

[![Selenium Java Tests](https://github.com/sdet-shiv-shelke/selenium-java-automation-framework/actions/workflows/maven.yml/badge.svg)](https://github.com/sdet-shiv-shelke/selenium-java-automation-framework/actions/workflows/maven.yml)
[![Selenium](https://img.shields.io/badge/Selenium-4.46-43B02A?logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![JUnit](https://img.shields.io/badge/JUnit-6.1-25A162?logo=junit5&logoColor=white)](https://junit.org/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

A production-style automation framework demonstrating maintainable **UI, API and database testing** with Selenium WebDriver and Java.

The project includes a deterministic local application and REST API written with the Java standard library. Tests do not depend on a public demo website, making execution stable and repeatable in local and CI environments.

## What this project demonstrates

- Page Object Model with reusable page and component classes
- ThreadLocal WebDriver management for parallel-safe execution
- Selenium Manager for automatic browser-driver resolution
- Chrome, Firefox, Edge and remote Selenium Grid support
- Explicit waits with zero implicit-wait mixing
- JUnit 6 tests, display names and smoke/regression tags
- REST API validation with REST Assured
- JDBC database validation with an H2 in-memory database
- Screenshots automatically captured when UI tests fail
- Environment and system-property configuration
- GitHub Actions browser matrix for Chrome and Firefox
- Maven reports and CI artifacts
- Dependabot maintenance for Maven and GitHub Actions

## Architecture

```text
selenium-java-automation-framework/
├── .github/
│   ├── dependabot.yml
│   └── workflows/maven.yml
├── src/
│   ├── main/java/io/github/sdetshiv/
│   │   ├── components/
│   │   ├── config/
│   │   ├── driver/
│   │   └── pages/
│   └── test/
│       ├── java/io/github/sdetshiv/
│       │   ├── support/
│       │   └── tests/
│       └── resources/junit-platform.properties
├── pom.xml
└── README.md
```

## Test coverage

| Layer | Coverage |
| --- | --- |
| UI smoke | Login, inventory loading and cart behavior |
| UI regression | Invalid login and complete checkout journey |
| API | Catalog response, order creation and request validation |
| Database | JDBC persistence and order-audit verification |

The complete suite contains **eight tests** and runs the UI coverage against both Chrome and Firefox in CI.

## Quick start

### Prerequisites

- Java 17 or newer
- Maven 3.9 or newer
- Chrome, Firefox or Edge

Selenium Manager downloads and configures the matching driver automatically.

### Install and run

```bash
git clone https://github.com/sdet-shiv-shelke/selenium-java-automation-framework.git
cd selenium-java-automation-framework
mvn clean verify
```

### Useful commands

```bash
# Chrome in headless mode (default)
mvn test

# Firefox
mvn test -Dbrowser=firefox

# Edge
mvn test -Dbrowser=edge

# Headed local execution
mvn test -Dheadless=false

# Smoke tests only
mvn test -Dgroups=smoke

# Regression tests only
mvn test -Dgroups=regression

# Remote Selenium Grid
mvn test -DremoteUrl=http://localhost:4444/wd/hub -Dbrowser=chrome
```

## Configuration

Configuration can be supplied through Java system properties or environment variables:

| System property | Environment variable | Default |
| --- | --- | --- |
| `browser` | `BROWSER` | `chrome` |
| `headless` | `HEADLESS` | `true` |
| `waitSeconds` | `WAIT_SECONDS` | `10` |
| `remoteUrl` | `REMOTE_URL` | Empty/local execution |

System properties take precedence over environment variables.

## Framework design

### Driver lifecycle

`DriverManager` uses `ThreadLocal<WebDriver>` so every parallel test thread receives an isolated browser. The JUnit extension creates the driver before each UI test and always quits it afterward.

### Page objects

Page classes expose business actions such as `loginAs()`, `addProduct()` and `checkout()`. Locators and synchronization stay inside the page layer so tests remain readable.

### Failure evidence

`UiTestExtension` captures a screenshot into `target/failure-screenshots` whenever a UI test fails. GitHub Actions uploads these screenshots and the Surefire reports as artifacts.

### Deterministic test application

`TestApplicationServer` starts on an available local port and serves the login, product, cart and checkout workflows together with product and order APIs. It is started and stopped automatically by JUnit extensions.

## CI/CD

GitHub Actions:

1. Sets up Temurin Java 21 and caches Maven dependencies.
2. Runs `mvn verify` against Chrome and Firefox in parallel matrix jobs.
3. Uploads Surefire reports for every run.
4. Uploads failure screenshots when a browser test fails.

## Author

**Shivshankar Shelke**  
Senior Software Test Engineer | QA Automation | SDET

- [GitHub](https://github.com/sdet-shiv-shelke)
- [LinkedIn](https://www.linkedin.com/in/shiv-shelke)

## License

This project is available under the [MIT License](LICENSE).
