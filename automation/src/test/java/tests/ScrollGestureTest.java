package tests;

import utils.GestureFlow;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.Map;

public class ScrollGestureTest extends GestureFlow {
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
}
