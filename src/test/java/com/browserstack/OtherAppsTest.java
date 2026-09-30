package com.browserstack;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.Activity;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

public class OtherAppsTest extends AppiumTest {

    public static void dismissOlderVersionWarning(AndroidDriver driver) {
        // Wrap in a try-catch so the test continues even if the dialog doesn't appear
        try {
            // Set a short wait
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

            // Locate the standard system "OK" or "Continue" button
            WebElement okButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    AppiumBy.id("android:id/button1")
            ));

            okButton.click();
            System.out.println("Dismissed the 'Older version of Android' warning dialog.");

        } catch (TimeoutException e) {
            System.out.println("Warning dialog did not appear; proceeding with the test.");
        }
    }

    @Test
    public void testOtherApps() throws InterruptedException {

        // Close Android Version Warning from old Wikipedia app, if present
        dismissOlderVersionWarning(driver);

        //Check if the apps specified under otherApps capability is installed on the device
        boolean isInstalled=driver.isAppInstalled("com.browserstack.demo.app");

        if(isInstalled) {
            //Switch to the app specified under otherApps capability
            Activity welcomeActivity = new Activity("com.browserstack.demo.app", "host.exp.exponent.MainActivity");
            driver.startActivity(welcomeActivity);

            // Add product to cart and view cart
            driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Add to cart\")")).click();
            driver.findElement(AppiumBy.accessibilityId("nav-cart")).click();

        } else {
            System.err.println("Package not found: com.browserstack.demo.app");
        }

        //Adding sleep for 2 seconds
        Thread.sleep(2000);

        //Switch back to the app to be tested
        Activity wpActivity = new Activity("org.wikipedia.alpha","org.wikipedia.main.MainActivity");
        driver.startActivity(wpActivity);

        //Adding sleep for 2 seconds
        Thread.sleep(2000);
    }
}
