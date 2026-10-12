
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

public class LoginTest extends BaseTest {

    private static final String APP_PACKAGE =
            "com.appiumpro.the_app";

    private final By loginScreenButton =
            By.xpath("//*[@content-desc='Login Screen']");

    private final By loginButton =
            By.xpath(
                    "//android.widget.Button[@content-desc='loginBtn']"
            );

    private final By logoutButton =
            By.xpath(
                    "//android.widget.Button[@content-desc='Logout']"
            );

    private final By secretArea =
            By.xpath("//*[@text='Secret Area']");

    private final By loggedInMessage =
            By.xpath(
                    "//*[@text='You are logged in as alice']"
            );

    private final By inputFields =
            By.className("android.widget.EditText");

    private final By invalidCredentialsMessage =
            By.id("android:id/message");

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

    private void openLogin() {

        // Restart without clearing application data
        driver.executeScript(
                "mobile: terminateApp",
                Map.of("appId", APP_PACKAGE)
        );

        driver.executeScript(
                "mobile: activateApp",
                Map.of("appId", APP_PACKAGE)
        );

        // Already on the login form
        if (isVisible(loginButton)) {
            System.out.println("LOGIN SCREEN READY");
            return;
        }

        // Open Login Screen from the home page
        waitForElements().until(
                ExpectedConditions.elementToBeClickable(
                        loginScreenButton
                )
        ).click();

        // The app may restore an existing login session
        if (isVisible(logoutButton)) {

            System.out.println(
                    "USER ALREADY LOGGED IN - LOGGING OUT"
            );

            Assert.assertTrue(
                    isVisible(secretArea),
                    "Logout button appeared outside Secret Area"
            );

            waitForElements().until(
                    ExpectedConditions.elementToBeClickable(
                            logoutButton
                    )
            ).click();

            System.out.println("LOGOUT CLICKED");
        }

        // Verify that the actual login form is ready
        waitForElements().until(
                ExpectedConditions.visibilityOfElementLocated(
                        loginButton
                )
        );

        waitForElements().until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        inputFields,
                        1
                )
        );

        System.out.println("LOGIN SCREEN OPENED");
    }

    private List<WebElement> getInputFields() {
        return waitForElements().until(
                ExpectedConditions.numberOfElementsToBeMoreThan(
                        inputFields,
                        1
                )
        );
    }

    private void fill(String username, String password) {

        List<WebElement> fields = getInputFields();

        fields.get(0).clear();
        fields.get(0).sendKeys(username);

        fields.get(1).clear();
        fields.get(1).sendKeys(password);
    }

    private void submitLogin() {
        waitForElements().until(
                ExpectedConditions.elementToBeClickable(
                        loginButton
                )
        ).click();
    }

    private void assertInvalidLoginMessage() {

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

    @Test
    public void validLoginShowsSuccess() throws Exception {

        try {
            openLogin();

            fill("alice", "mypassword");

            submitLogin();

            waitForElements().until(
                    ExpectedConditions.visibilityOfElementLocated(
                            loggedInMessage
                    )
            );

            Assert.assertTrue(
                    isVisible(secretArea),
                    "Secret Area was not displayed after login"
            );

            Assert.assertTrue(
                    isVisible(logoutButton),
                    "Logout button was not displayed after login"
            );

            System.out.println(
                    "VALID LOGIN NAVIGATION PASSED"
            );

        } finally {
            evidence("login-valid");
        }
    }

    @Test
    public void invalidLoginShowsError() throws Exception {

        try {
            openLogin();

            fill("wronguser", "wrongpass");

            submitLogin();

            assertInvalidLoginMessage();

            System.out.println(
                    "INVALID LOGIN TEST PASSED"
            );

        } finally {
            evidence("login-invalid");
        }
    }

    @Test
    public void emptyLoginShowsError() throws Exception {

        try {
            openLogin();

            // Ensure both fields are actually empty
            List<WebElement> fields = getInputFields();

            fields.get(0).clear();
            fields.get(1).clear();

            submitLogin();

            assertInvalidLoginMessage();

            System.out.println(
                    "EMPTY LOGIN TEST PASSED"
            );

        } finally {
            evidence("login-empty");
        }
    }
}
