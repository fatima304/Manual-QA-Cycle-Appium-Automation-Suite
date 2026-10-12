
package utils;

import base.BaseTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public abstract class LoginFlow extends BaseTest {

    private static final String APP_PACKAGE =
            "com.appiumpro.the_app";

    private final By loginScreenButton =
            By.xpath("//*[@content-desc='Login Screen']");

    private final By loginButton =
            By.xpath(
                    "//android.widget.Button[@content-desc='loginBtn']"
            );

    protected final By logoutButton =
            By.xpath(
                    "//android.widget.Button[@content-desc='Logout']"
            );

    protected final By secretArea =
            By.xpath("//*[@text='Secret Area']");

    protected final By loggedInMessage =
            By.xpath(
                    "//*[@text='You are logged in as alice']"
            );

    private final By inputFields =
            By.className("android.widget.EditText");

    private final By invalidCredentialsMessage =
            By.id("android:id/message");

    protected WebDriverWait waitForElements() {
        return new WebDriverWait(
                driver,
                Duration.ofSeconds(20)
        );
    }

    protected boolean isVisible(By locator) {
        return driver.findElements(locator)
                .stream()
                .anyMatch(WebElement::isDisplayed);
    }

    protected void openLogin() {
        HomeNavigation.openMenu(driver, loginScreenButton);

        if (isVisible(logoutButton)) {
            Assert.assertTrue(isVisible(secretArea), "Logout button appeared outside Secret Area");
            waitForElements().until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
        }
        waitForElements().until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        waitForElements().until(ExpectedConditions.numberOfElementsToBeMoreThan(inputFields, 1));
        System.out.println("LOGIN SCREEN OPENED");
    }

    protected void clearInputAt(int index) {
        // Always locate the field again after the UI changes to avoid stale references.
        WebElement field = getInputFields().get(index);
        if (!field.getText().isEmpty()) {
            field.clear();
        }
    }

    protected List<WebElement> getInputFields() {
        return waitForElements().until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        inputFields,
                        1
                )
        );
    }

    protected void fill(String username, String password) {

        clearInputAt(0);
        getInputFields().get(0).sendKeys(username);
        clearInputAt(1);
        getInputFields().get(1).sendKeys(password);
    }

    protected void submitLogin() {
        waitForElements().until(
                ExpectedConditions.elementToBeClickable(
                        loginButton
                )
        ).click();
    }

    protected void assertInvalidLoginMessage() {

        WebElement errorMessage = waitForElements().until(
                ExpectedConditions.visibilityOfElementLocated(
                        invalidCredentialsMessage
                )
        );

        Assert.assertEquals(
                errorMessage.getText(),
                "Invalid login credentials, please try again",
                "Unexpected login error message"
        );
    }

}
