package tests;

import utils.LoginFlow;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class InvalidLoginTest extends LoginFlow {
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
}
