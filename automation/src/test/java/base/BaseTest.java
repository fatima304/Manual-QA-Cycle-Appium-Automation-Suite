package base;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import utils.CapabilitiesHelper;
import java.net.URI;
import java.time.Duration;
import java.nio.file.*;
public abstract class BaseTest {
 protected AndroidDriver driver;
 @BeforeMethod(alwaysRun=true) public void setUp() throws Exception {
  driver=new AndroidDriver(URI.create(System.getProperty("appiumUrl","http://127.0.0.1:4723")).toURL(),CapabilitiesHelper.options());
 }
 protected WebElement text(String value) {return new WebDriverWait(driver,Duration.ofSeconds(15)).until(d->d.findElement(AppiumByText.exact(value)));}
 protected WebElement xpath(String value) {return new WebDriverWait(driver,Duration.ofSeconds(15)).until(d->d.findElement(By.xpath(value)));}
 protected void evidence(String name) throws Exception {
  Path dir=Path.of("target","screenshots");Files.createDirectories(dir);
  Files.copy(((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE).toPath(),dir.resolve(name+".png"),StandardCopyOption.REPLACE_EXISTING);
 }
 @AfterMethod(alwaysRun=true) public void tearDown(){if(driver!=null)driver.quit();}
 // Native UIAutomator text locator; validate against Appium Inspector on v1.12.0.
 private static final class AppiumByText {
  static By exact(String value){return io.appium.java_client.AppiumBy.androidUIAutomator("new UiSelector().text(\""+value+"\")");}
 }
}
