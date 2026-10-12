package tests;

import utils.GestureFlow;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.Map;

public class TapGestureTest extends GestureFlow {
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
}
