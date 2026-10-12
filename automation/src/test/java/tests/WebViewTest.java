
package tests;

import base.BaseTest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.Set;

public class WebViewTest extends BaseTest {

    @Test
    public void permittedWebViewCanInteract() throws Exception {

        boolean switchedToWebView = false;

        String testUrl = "https://appiumpro.com";

        try {
            WebDriverWait wait = new WebDriverWait(
                    driver,
                    Duration.ofSeconds(30)
            );

            // 1. Open Webview Demo screen
            text("Webview Demo").click();

            // 2. Locate URL input
            WebElement urlInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath(
                                    "//android.widget.EditText[@content-desc='urlInput']"
                            )
                    )
            );

            // 3. Enter URL
            urlInput.clear();
            urlInput.sendKeys(testUrl);

            Assert.assertEquals(
                    urlInput.getText().trim(),
                    testUrl,
                    "Incorrect URL entered"
            );

            // 4. Click Go button
            WebElement goButton = wait.until(
                    ExpectedConditions.elementToBeClickable(
                            By.xpath(
                                    "//android.widget.Button[@content-desc='navigateBtn']"
                            )
                    )
            );

            goButton.click();

            System.out.println("GO BUTTON CLICKED");

            // 5. Wait for WEBVIEW context
            wait.until(webDriver -> {
                Set<String> contexts = driver.getContextHandles();

                for (String context : contexts) {
                    if (context.startsWith("WEBVIEW")) {
                        return true;
                    }
                }

                return false;
            });

            // 6. Find WEBVIEW context
            String webviewContext = null;

            for (String context : driver.getContextHandles()) {
                if (context.startsWith("WEBVIEW")) {
                    webviewContext = context;
                    break;
                }
            }

            Assert.assertNotNull(
                    webviewContext,
                    "WEBVIEW context was not found"
            );

            // 7. Switch to WEBVIEW
            driver.context(webviewContext);
            switchedToWebView = true;

            System.out.println(
                    "SWITCHED TO: " + webviewContext
            );

            // 8. Wait for a real webpage to load
            try {
                wait.until(webDriver -> {
                    String currentUrl = driver.getCurrentUrl();

                    return currentUrl != null
                            && !currentUrl.trim().isEmpty()
                            && !currentUrl.equals("about:blank");
                });

            } catch (TimeoutException e) {

                System.out.println("PAGE LOAD TIMEOUT");
                System.out.println(
                        "CURRENT URL: " + driver.getCurrentUrl()
                );

                throw e;
            }

            String loadedUrl = driver.getCurrentUrl();

            System.out.println("REQUESTED URL: " + testUrl);
            System.out.println("ACTUAL LOADED URL: " + loadedUrl);
            System.out.println("PAGE TITLE: " + driver.getTitle());

            // 9. Verify the webpage contains visible text
            WebElement pageBody = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.tagName("body")
                    )
            );

            wait.until(webDriver ->
                    !pageBody.getText().trim().isEmpty()
            );

            Assert.assertFalse(
                    pageBody.getText().trim().isEmpty(),
                    "Web page has no visible text"
            );

            System.out.println("WEB PAGE CONTENT VERIFIED");

            // 10. Find HTML links
            List<WebElement> links = wait.until(
                    ExpectedConditions.presenceOfAllElementsLocatedBy(
                            By.cssSelector("a[href]")
                    )
            );

            WebElement selectedLink = null;

            // 11. Select a visible, enabled HTML link
            for (WebElement link : links) {

                String href = link.getAttribute("href");

                if (link.isDisplayed()
                        && link.isEnabled()
                        && href != null
                        && href.startsWith("http")
                        && !href.equals(driver.getCurrentUrl())) {

                    selectedLink = link;
                    break;
                }
            }

            Assert.assertNotNull(
                    selectedLink,
                    "No suitable clickable HTML link was found"
            );

            // 12. Save current URL and window count
            String urlBeforeClick = driver.getCurrentUrl();
            int windowsBeforeClick = driver.getWindowHandles().size();

            String selectedLinkUrl =
                    selectedLink.getAttribute("href");

            System.out.println(
                    "SELECTED LINK: " + selectedLinkUrl
            );

            // 13. Click HTML link
            selectedLink.click();

            // 14. Verify interaction caused navigation
            boolean interactionSucceeded = wait.until(webDriver -> {

                String currentUrl = driver.getCurrentUrl();

                return (currentUrl != null
                        && !currentUrl.equals(urlBeforeClick))
                        || driver.getWindowHandles().size()
                        > windowsBeforeClick;
            });

            Assert.assertTrue(
                    interactionSucceeded,
                    "HTML link click did not trigger navigation"
            );

            System.out.println("WEBVIEW INTERACTION PASSED");

            System.out.println(
                    "FINAL URL: " + driver.getCurrentUrl()
            );

        } finally {

            // 15. Return to native context
            if (driver != null) {

                try {
                    if (switchedToWebView) {
                        driver.context("NATIVE_APP");
                    }

                } finally {

                    // 16. Save screenshot evidence
                    evidence("webview-interaction");
                }
            }
        }
    }
}