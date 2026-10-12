package utils;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/** Returns to TheApp's main menu without clearing app data on real devices. */
public final class HomeNavigation {
    private static final String APP_PACKAGE = "com.appiumpro.the_app";
    private static final By LOGIN_MENU = By.xpath("//*[@content-desc='Login Screen']");
    private static final By LIST_MENU = By.xpath("//*[@content-desc='List Demo']");
    private static final By WEBVIEW_MENU = By.xpath("//*[@text='Webview Demo']");
    private static final By UP_BUTTON = By.xpath("//*[@content-desc='Navigate Up']");

    private HomeNavigation() {}

    private static boolean onHome(AndroidDriver driver) {
        return !driver.findElements(LOGIN_MENU).isEmpty()
                || !driver.findElements(LIST_MENU).isEmpty()
                || !driver.findElements(WEBVIEW_MENU).isEmpty();
    }

    public static void openHome(AndroidDriver driver) {
        // A real Android phone can retain the last screen because noReset=true.
        driver.context("NATIVE_APP");
        driver.executeScript("mobile: terminateApp", Map.of("appId", APP_PACKAGE));
        driver.executeScript("mobile: activateApp", Map.of("appId", APP_PACKAGE));

        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
        for (int attempt = 0; attempt < 8; attempt++) {
            if (onHome(driver)) return;
            List<WebElement> up = driver.findElements(UP_BUTTON);
            if (!up.isEmpty()) {
                up.get(0).click();
            } else {
                driver.navigate().back();
            }
            try {
                shortWait.until(d -> onHome(driver));
                return;
            } catch (org.openqa.selenium.TimeoutException ignored) {
                // Some screens need more than one Back action.
            }
        }
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> onHome(driver));
    }

    public static void openMenu(AndroidDriver driver, By locator) {
        openHome(driver);
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.elementToBeClickable(locator)).click();
    }
}
