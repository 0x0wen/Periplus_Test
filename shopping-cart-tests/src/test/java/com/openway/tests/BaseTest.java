package com.openway.tests;

import com.openway.pages.HomePage;
import com.openway.utils.ConfigManager;
import com.openway.utils.DriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.logging.Logger;

public class BaseTest {
    protected WebDriver driver;
    protected HomePage homePage;
    protected final Logger logger = Logger.getLogger(this.getClass().getName());
    
    @BeforeClass
    @Parameters({"browser", "headless"})
    public void setUp(@Optional("chrome") String browser, @Optional("false") String headless) {
        logger.info("Setting up test environment");
        
        ConfigManager.init();
        
        boolean isHeadless = Boolean.parseBoolean(headless);
        driver = DriverFactory.createDriver(browser, isHeadless);
        
        homePage = new HomePage(driver);
        
        logger.info("Test environment setup complete");
    }
    
    @AfterClass
    public void tearDown() {
        logger.info("Tearing down test environment");
        
        if (driver != null) {
            driver.quit();
        }
        
        logger.info("Test environment cleanup complete");
    }
}