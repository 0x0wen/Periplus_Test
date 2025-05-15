package com.openway.pages;

import java.time.Duration;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected final Logger logger = Logger.getLogger(this.getClass().getName());
    
    /**
     * Constructor to initialize page elements and the WebDriver
     * 
     * @param driver the WebDriver instance
     */
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        PageFactory.initElements(driver, this);
    }
    
    /**
     * Wait for element to be clickable and then click
     *
     * @param element the web element to click
     */
    protected void clickElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }
    
    /**
     * Wait for element to be visible and then enter text
     *
     * @param element the web element to enter text into
     * @param text the text to enter
     */
    protected void enterText(WebElement element, String text) {
        wait.until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(text);
    }
    
    /**
     * Wait for page load to complete by checking for absence of preloader
     */
    protected void waitForPageLoad() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
    }
    
    /**
     * Check if an element exists on the page
     *
     * @param locator the By locator for the element
     * @return true if element exists, false otherwise
     */
    protected boolean isElementPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }
    
    /**
     * Wait for element to be visible
     *
     * @param element the web element to wait for
     */
    protected void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    /**
     * Wait for element to be clickable
     *
     * @param element the web element to wait for
     */
    protected void waitForClickability(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }
}