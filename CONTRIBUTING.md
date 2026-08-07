# Contributing

Thank you for your interest in the Selenium Java Automation Framework.

## Development setup

1. Install Java 17 or newer, Maven 3.9+, and Chrome or Firefox.
2. Clone the repository and create a focused feature branch.
3. Run the complete verification suite before opening a pull request:

```bash
mvn clean verify
```

## Test design guidelines

- Keep business actions in page objects and assertions in test classes.
- Prefer stable `data-testid` locators and explicit waits.
- Make every test independent, deterministic, and safe for parallel execution.
- Add `smoke` or `regression` tags where appropriate.
- Include API or database checks when they provide meaningful coverage.
- Never commit credentials, generated reports, screenshots, or browser drivers.

## Pull requests

Describe what changed, why it matters, and how it was verified. Keep each pull request focused and ensure the GitHub Actions browser matrix passes.
