# TheApp — Manual QA & Appium Automation Suite

Mobile QA project for **TheApp (Android)**, combining manual test design, defect tracking, and Java/Appium UI automation on Android emulators and a physical device.

## Scope

- Manual QA: Login, List Demo, Echo Box, Gestures, and general mobile behavior; test cases, traceability, execution evidence, Jira defects, and closure reporting.
- Automated Login: valid, invalid, and empty credentials.
- Automated gestures: Tap, Scroll, and Swipe with UI assertions.
- Hybrid WebView: native-to-WebView context switching and interaction with an HTML element.
- Physical Android execution of Login and Gesture tests.

## Tech Stack

Java 17, Appium 3.8.0, UiAutomator2 8.7.0, Appium Java Client, Selenium, TestNG, Maven, Android SDK/ADB, Jira, and Git/GitHub.

## Project Structure

```text
.
├── TheApp.apk
├── automation/
│   ├── pom.xml
│   └── src/test/java/
│       ├── base/
│       │   └── BaseTest.java
│       ├── tests/
│       │   ├── ValidLoginTest.java
│       │   ├── InvalidLoginTest.java
│       │   ├── EmptyLoginTest.java
│       │   ├── TapGestureTest.java
│       │   ├── ScrollGestureTest.java
│       │   ├── SwipeGestureTest.java
│       │   ├── SlideGestureTest.java          # Disabled
│       │   ├── DragAndDropGestureTest.java    # Disabled
│       │   └── WebViewTest.java
│       └── utils/
│           └── CapabilitiesHelper.java
└── README.md
```

Other supporting helpers may be present in the Java source tree. The `automation/target/` directory contains generated Maven reports and should not be committed.

## Prerequisites

Install JDK 17, Maven, Node.js, Android SDK Platform Tools, Appium 3, and the UiAutomator2 driver. Start an Android emulator or connect an unlocked phone with USB debugging enabled.

Confirm device connectivity:

```powershell
adb devices
```

Run Appium Doctor and keep a screenshot of its output as setup evidence.

## Start Appium

In a separate terminal, run:

```powershell
appium --allow-insecure uiautomator2:chromedriver_autodownload
```

Appium server: `http://127.0.0.1:4723/`.

Only enable automatic Chromedriver downloads in a trusted local environment.

## Run Automated Tests

Open PowerShell in the repository root and navigate to the automation directory:

```powershell
cd automation
```

### Run the suite on a physical Android device

Replace `YOUR_DEVICE_UDID` with the ID shown by `adb devices`:

```powershell
mvn --% test -DappPath=../TheApp.apk -Dudid=YOUR_DEVICE_UDID -DdeviceName=RealAndroidPhone
```

### Run individual scenarios

```powershell
mvn --% test -Dtest=ValidLoginTest -DappPath=../TheApp.apk -Dudid=YOUR_DEVICE_UDID -DdeviceName=RealAndroidPhone
mvn --% test -Dtest=InvalidLoginTest -DappPath=../TheApp.apk -Dudid=YOUR_DEVICE_UDID -DdeviceName=RealAndroidPhone
mvn --% test -Dtest=EmptyLoginTest -DappPath=../TheApp.apk -Dudid=YOUR_DEVICE_UDID -DdeviceName=RealAndroidPhone
mvn --% test -Dtest=TapGestureTest -DappPath=../TheApp.apk -Dudid=YOUR_DEVICE_UDID -DdeviceName=RealAndroidPhone
mvn --% test -Dtest=ScrollGestureTest -DappPath=../TheApp.apk -Dudid=YOUR_DEVICE_UDID -DdeviceName=RealAndroidPhone
mvn --% test -Dtest=SwipeGestureTest -DappPath=../TheApp.apk -Dudid=YOUR_DEVICE_UDID -DdeviceName=RealAndroidPhone
mvn --% test -Dtest=WebViewTest -DappPath=../TheApp.apk -Dudid=YOUR_DEVICE_UDID -DdeviceName=RealAndroidPhone
```

For an emulator, use its ADB device ID instead of the physical phone ID. `--%` is Windows PowerShell stop-parsing syntax; other shells may need different argument handling.

## Automation Results

| Scenario | Status | Notes |
| --- | --- | --- |
| Valid Login | Passed | Real-device execution |
| Invalid Login | Passed | Real-device execution |
| Empty Login | Passed | Real-device execution |
| Tap | Passed | Real-device execution |
| Scroll | Passed | Real-device execution |
| Swipe | Passed | Real-device execution |
| WebView | Passed with limitation | Context switching and HTML interaction succeeded; intended website content was not verified |
| Slide | Blocked / Disabled | No suitable slider target verified in TheApp v1.12.0 |
| Drag & Drop | Blocked / Disabled | No suitable drag-and-drop target verified in TheApp v1.12.0 |

**WebView limitation:** The default `https://appiumpro.com` address redirected to unrelated external content. This does not verify the intended AppiumPro webpage, although WebView context switching and HTML interaction were exercised.

**Gesture limitation:** Slide and Drag & Drop are explicit requested scenarios, but they were not executed successfully and must not be counted as passed.

## Real-Device Notes

Physical device: **realme RMX3840**, Android 16 (API 36). The phone initially restricted automated app installation and app-data clearing. The APK was installed manually, and the test configuration used `noReset=true` to avoid the data-clearing restriction. These are device/environment limitations, not confirmed application defects.

## Assertions and Evidence

- Valid Login: successful login state, Secret Area, and Logout control.
- Invalid / Empty Login: displayed invalid-credentials feedback.
- Tap / Scroll / Swipe: observable UI changes.
- WebView: context switch and interaction with an HTML element, subject to the external redirect limitation.

Generated TestNG/Maven reports are found under `automation/target/surefire-reports/`. Keep screenshots of Appium Doctor, test execution, and physical-device runs in the submitted evidence materials. Do not claim screenshots or Jira defects that have not actually been captured or filed.

## Manual QA Deliverables

- One-page test plan.
- Test cases with execution results and evidence.
- Requirements traceability matrix.
- Jira defect reports with reproducible steps, expected/actual behavior, environment, severity, and screenshots.
- Test closure summary using actual execution metrics.

Only independently observed defects should be reported as bugs. Blocked or unexecuted tests must not be counted as passed.
