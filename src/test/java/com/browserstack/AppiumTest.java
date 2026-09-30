package com.browserstack;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.browserstack.config.BrowserStackConfig;
import org.openqa.selenium.MutableCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;


public class AppiumTest {

    public AndroidDriver driver;

    @BeforeMethod(alwaysRun=true)
    public void setUp() throws Exception {
        MutableCapabilities capabilities = new UiAutomator2Options();
        capabilities.setCapability("appium:autoAcceptAlerts",true);

        // Uncomment below if not using SDK, in order to set the "otherApps" capability directly
        // capabilities.setCapability("otherApps", new String[]{"bs://6fac8edd79e2ecf33f016374ad49809abf7c2881"});

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),capabilities);
    }

    @AfterMethod(alwaysRun=true)
    public void tearDown() throws Exception {
        driver.quit();
    }
}
