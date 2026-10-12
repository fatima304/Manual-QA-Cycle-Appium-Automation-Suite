package tests;

import utils.LoginFlow;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class ValidLoginTest extends LoginFlow {
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
}
