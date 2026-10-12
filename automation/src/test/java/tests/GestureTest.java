
package tests;

import base.BaseTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class GestureTest extends BaseTest {

    private static final String APP_PACKAGE =
            "com.appiumpro.the_app";

    private final By listDemo =
            By.xpath("//*[@content-desc='List Demo']");

    private final By navigateUp =
            By.xpath("//*[@content-desc='Navigate Up']");

    private WebDriverWait waitForElements() {
        return new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
    }

    private boolean isVisible(By locator) {
        return driver.findElements(locator)
                .stream()
                .anyMatch(WebElement::isDisplayed);
    }

    private void openList() {

        // Restart app without clearing application data
        driver.executeScript(
                "mobile: terminateApp",
                Map.of("appId", APP_PACKAGE)
        );

        driver.executeScript(
                "mobile: activateApp",
                Map.of("appId", APP_PACKAGE)
        );

        // Return to Home if another screen is open
        for (int attempt = 0; attempt < 5; attempt++) {

            if (isVisible(listDemo)) {
                break;
            }

            List<WebElement> backButtons =
                    driver.findElements(navigateUp);

            if (!backButtons.isEmpty()) {
                backButtons.get(0).click();
            } else {
                driver.navigate().back();
            }
        }

        // Confirm Home screen is available
        waitForElements().until(
                ExpectedConditions.elementToBeClickable(
                        listDemo
                )
        ).click();

        // Confirm navigation away from Home
        waitForElements().until(
                ExpectedConditions.invisibilityOfElementLocated(
                        listDemo
                )
        );

        System.out.println("LIST DEMO OPENED");
    }

    @Test
    public void tapOpensListDetail() throws Exception {

        try {
            openList();

            List<WebElement> items =
                    driver.findElements(
                            By.className("android.widget.TextView")
                    );

            Assert.assertTrue(
                    items.size() > 1,
                    "No list items found"
            );

            String before = driver.getPageSource();

            items.get(items.size() - 1).click();

            String after = driver.getPageSource();

            Assert.assertNotEquals(
                    after,
                    before,
                    "Tap did not change the UI"
            );

            System.out.println("TAP GESTURE PASSED");

        } finally {
            evidence("gesture-tap");
        }
    }

    @Test
    public void scrollMovesList() throws Exception {

        try {
            openList();

            String before = driver.getPageSource();

            Object result = driver.executeScript(
                    "mobile: scrollGesture",
                    Map.of(
                            "left", 80,
                            "top", 350,
                            "width", 850,
                            "height", 1100,
                            "direction", "down",
                            "percent", 0.8
                    )
            );

            System.out.println(
                    "Scroll gesture result: " + result
            );

            String after = driver.getPageSource();

            Assert.assertNotEquals(
                    after,
                    before,
                    "Scroll did not change the visible UI"
            );

            System.out.println("SCROLL GESTURE PASSED");

        } finally {
            evidence("gesture-scroll");
        }
    }

    @Test
    public void swipeMovesList() throws Exception {

        try {
            openList();

            String before = driver.getPageSource();

            driver.executeScript(
                    "mobile: swipeGesture",
                    Map.of(
                            "left", 80,
                            "top", 350,
                            "width", 850,
                            "height", 1100,
                            "direction", "up",
                            "percent", 0.8
                    )
            );

            String after = driver.getPageSource();

            Assert.assertNotEquals(
                    after,
                    before,
                    "Swipe did not change the visible UI"
            );

            System.out.println("SWIPE GESTURE PASSED");

        } finally {
            evidence("gesture-swipe");
        }
    }

    @Test(
            enabled = false,
            description = "BLOCKED: No verified slider in TheApp v1.12.0"
    )
    public void slideChangesSliderValue() {

        Assert.fail(
                "Slider element not available"
        );
    }

    @Test(
            enabled = false,
            description = "BLOCKED: No verified drag-and-drop targets in TheApp v1.12.0"
    )
    public void dragAndDropMovesElement() {

        Assert.fail(
                "Drag-and-drop targets not available"
        );
    }
}
