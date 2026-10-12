package tests;

import utils.GestureFlow;
import org.testng.annotations.Test;
import org.testng.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.Map;

public class SwipeGestureTest extends GestureFlow {
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
}
