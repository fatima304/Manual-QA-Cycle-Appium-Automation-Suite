
package utils;

import io.appium.java_client.android.options.UiAutomator2Options;

import java.nio.file.Path;
import java.time.Duration;

public final class CapabilitiesHelper {

    private CapabilitiesHelper() {}

    public static UiAutomator2Options options() {

        UiAutomator2Options o = new UiAutomator2Options();

        o.setPlatformName("Android");
        o.setAutomationName("UiAutomator2");

        o.setDeviceName(
                System.getProperty("deviceName", "Android Emulator")
        );

        String udid = System.getProperty("udid", "");

        if (!udid.isBlank()) {
            o.setUdid(udid);
        }

        // APK path
        o.setApp(
                Path.of(
                        System.getProperty("appPath", "../TheApp.apk")
                ).toAbsolutePath().normalize().toString()
        );

        o.setNewCommandTimeout(Duration.ofSeconds(120));

        // Prevent clearing application data on the real device
        // while keeping the emulator's previous behavior
        boolean noReset = Boolean.parseBoolean(
                System.getProperty(
                        "noReset",
                        String.valueOf(!udid.isBlank()
                                && !udid.startsWith("emulator-"))
                )
        );

        o.setNoReset(noReset);

        return o;
    }
}
