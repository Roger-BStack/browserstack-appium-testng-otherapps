# BrowserStack Appium TestNG — otherApps Sample

This project demonstrates how to use the BrowserStack **`otherApps`** feature with Appium and TestNG (Java). It shows two ways to specify additional apps that should be pre-installed on the device alongside your main app under test.

---

## Overview

The test launches the **Wikipedia** app (`WikipediaSample-2.5.apk`) as the primary app, then switches to the **BrowserStack Demo App** (pre-installed via `otherApps`), adds a product to the cart, and switches back to Wikipedia — verifying that multiple apps can be installed and driven in a single session.

---

## Project Structure

```
.
├── browserstack.yml                                          # BrowserStack SDK configuration
├── browserstack-demoapp.apk                                  # Demo app (local copy)
├── WikipediaSample-2.5.apk                                   # Primary app under test
├── pom.xml                                                   # Maven build (otherapps-test profile)
├── build.gradle                                              # Gradle build
├── README.md                                                 # Project details (this file)
└── src/test/
    ├── java/com/browserstack/
    │   ├── AppiumTest.java                                   # Base test class (driver setup/teardown)
    │   └── OtherAppsTest.java                               # Test: switch between apps
    └── resources/com/browserstack/
        └── otherapps-test.testng.xml                        # TestNG suite definition
```

---

## Prerequisites

- Java 11+
- Maven **or** Gradle
- A [BrowserStack App Automate](https://app-automate.browserstack.com/) account
- Your primary app and any `otherApps` already uploaded to BrowserStack (see [Upload an app](https://www.browserstack.com/docs/app-automate/appium/getting-started/java/integrate-your-tests?fw-lang=java#:~:text=%3C/dependencies%3E-,Upload%20app,-Upload%20your%20Android))

---

## Configuration

### Credentials

Set your BrowserStack credentials as environment variables (recommended) or replace the placeholders in `browserstack.yml`:

```bash
export BROWSERSTACK_USERNAME=<your-username>
export BROWSERSTACK_ACCESS_KEY=<your-access-key>
```

---

## Specifying `otherApps` — Two Approaches

The `otherApps` feature tells BrowserStack to pre-install one or more additional apps on the device before the session starts. You can configure this in **either** of two places.

### Approach 1 — `browserstack.yml` (recommended with the BrowserStack SDK)

Add the `otherApps` key directly to `browserstack.yml`. The BrowserStack Java SDK reads this file and passes the capability automatically — no code changes required.

```yaml
# browserstack.yml

app: ./WikipediaSample-2.5.apk

otherApps:
  - bs://6fac8edd79e2ecf33f016374ad49809abf7c2881
```

Each entry is a `bs://` URL returned when you upload an app to BrowserStack. You can list multiple apps:

```yaml
otherApps:
  - bs://6fac8edd79e2ecf33f016374ad49809abf7c2881
  - bs://another_app_hash_here
```

**This is the active configuration in this project.** The commented-out line in `AppiumTest.java` is intentionally left in place to illustrate the alternative.

---

### Approach 2 — Capability in `AppiumTest.java` (without the SDK, or for overrides)

If you are **not** using the BrowserStack SDK (i.e. connecting directly to the Appium server), set the capability programmatically in the `setUp()` method of `AppiumTest.java`:

```java
// AppiumTest.java — BeforeMethod

// Uncomment below if not using SDK, in order to set the "otherApps" capability directly
capabilities.setCapability("otherApps", new String[]{"bs://6fac8edd79e2ecf33f016374ad49809abf7c2881"});
```

This line is already present but commented out in `AppiumTest.java`. Uncomment it when running without the SDK.

> **Important:** Do not set `otherApps` in both places at the same time. When using the SDK, `browserstack.yml` is the single source of truth; the in-code capability would conflict.

---

### Comparison

| | `browserstack.yml` | Capability in code |
|---|---|---|
| **Requires BrowserStack SDK** | Yes | No |
| **Change apps without editing code** | ✅ Yes | ❌ No |
| **Works in CI via env vars / config file** | ✅ Yes | Requires code change |
| **Supports multiple apps** | ✅ Yes (list) | ✅ Yes (String array) |
| **Recommended for** | SDK-based runs | Direct Appium / non-SDK runs |

---

## Running the Tests

### Maven

```bash
mvn test -P otherapps-test \
  -DBROWSERSTACK_USERNAME=$BROWSERSTACK_USERNAME \
  -DBROWSERSTACK_ACCESS_KEY=$BROWSERSTACK_ACCESS_KEY
```

### Gradle

```bash
./gradlew otherAppsTest \
  -DBROWSERSTACK_USERNAME=$BROWSERSTACK_USERNAME \
  -DBROWSERSTACK_ACCESS_KEY=$BROWSERSTACK_ACCESS_KEY
```

---

## What the Test Does

1. **Launches** the Wikipedia app (primary `app` in `browserstack.yml`).
2. **Dismisses** any "older Android version" warning dialog if present.
3. **Checks** whether `com.browserstack.demo.app` (the `otherApps` entry) is installed.
4. **Switches** to the demo app, adds a product to the cart, and opens the cart.
5. **Switches back** to the Wikipedia app.

---

## Uploading Apps to BrowserStack

```bash
curl -u "$BROWSERSTACK_USERNAME:$BROWSERSTACK_ACCESS_KEY" \
  -X POST "https://api-cloud.browserstack.com/app-automate/upload" \
  -F "file=@./browserstack-demoapp.apk"
```

The response contains the `app_url` (`bs://...`) to use in `otherApps`.

---

## References

- [Appium Java Client](https://github.com/appium/java-client)