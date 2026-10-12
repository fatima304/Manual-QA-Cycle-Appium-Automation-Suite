package tests;

import utils.LoginFlow;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;

public class EmptyLoginTest extends LoginFlow {
    @Test
    public void emptyLoginShowsError() throws Exception {

        try {
            openLogin();

            // Ensure both fields are actually empty
            clearInputAt(0);
            clearInputAt(1);

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
