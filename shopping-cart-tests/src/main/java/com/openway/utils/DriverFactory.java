package com.openway.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.logging.Logger;

public class DriverFactory {
    private static final Logger logger = Logger.getLogger(DriverFactory.class.getName());
    
    private DriverFactory() {
        // Private constructor to prevent instantiation
    }
    
    
    public static WebDriver createDriver(String browserName, boolean headless) {
        WebDriver driver;
        browserName = browserName.toLowerCase();
        
        logger.info("Creating " + browserName + " driver" + (headless ? " in headless mode" : ""));
        
        switch (browserName) {
            case "chrome":
                driver = createChromeDriver(headless);
                break;
            default:
                logger.warning("Unknown browser: " + browserName + ". Defaulting to Chrome.");
                driver = createChromeDriver(headless);
        }
        
        driver.manage().window().maximize();
        return driver;
    }
    
    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        
        if (headless) {
            options.addArguments("--headless");
        }
        
        return new ChromeDriver(options);
    }
  
}