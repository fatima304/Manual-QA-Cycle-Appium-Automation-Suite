package utils;

import base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public abstract class GestureFlow extends BaseTest {
    private final By listDemo = By.xpath("//*[@content-desc='List Demo']");

    protected void openList() {
        HomeNavigation.openMenu(driver, listDemo);
        new WebDriverWait(driver, Duration.ofSeconds(20)).until(
                ExpectedConditions.invisibilityOfElementLocated(listDemo));
        System.out.println("LIST DEMO OPENED");
    }
}
