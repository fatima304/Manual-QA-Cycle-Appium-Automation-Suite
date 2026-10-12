# TheApp QA Capstone — Appium Automation

Java 17 + Maven + TestNG + Appium Java Client 10.1.1 + UiAutomator2.

## Requirements
- Android Studio emulator or USB-debug-enabled real device
- Java 17, Maven, Node.js, Appium 2/3 with UiAutomator2 driver
- TheApp v1.12.0 APK from https://github.com/appium-pro/TheApp/releases/tag/v1.12.0

## Setup (Windows PowerShell)
1. Put `TheApp.apk` next to the `automation` folder (or set `-DappPath=...`).
2. `adb devices` — confirm emulator/device listed.
3. `appium driver list --installed` — confirm uiautomator2.
4. Start Appium server: `appium` (keep terminal open).
5. In the `automation` folder run:
   `mvn test -DappPath=../TheApp.apk`
6. For physical device: `mvn test -DappPath=../TheApp.apk -Dudid=YOUR_DEVICE_UDID`

Screenshots are saved to `target/screenshots/` for tests that reach the evidence step.

## Important validation notes
This is a **starter suite**, not a verified passing suite. It was not executed on the user's Windows emulator. Verify text labels, native element locators, and success assertions with Appium Inspector. The login success assertion currently only excludes an error message; replace with a positive destination assertion. WebView test intentionally skips until URL navigation, WEBVIEW context and web-element locators are inspected. Slide and drag-and-drop tests are disabled because the inspected home screen did not show gesture targets; do not claim them as passed. The user must capture real execution screenshots and appium-doctor output.

## Test organization
Shared driver lifecycle: `base/BaseTest.java`. Capabilities: `utils/CapabilitiesHelper.java`. Scenarios: `tests/LoginTest.java`, `tests/GestureTest.java`, `tests/WebViewTest.java`.
