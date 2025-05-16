package com.openway.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Page object for the Home page of Periplus website
 */
public class HomePage extends BasePage {
    private PageLayout pageLayout = new PageLayout(driver);

    @FindBy(id = "filter_name")
    private WebElement searchBox;
    
    @FindBy(css = ".search-bar button[type='submit']")
    private WebElement searchButton;
    
    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Navigate to home page
     *
     * @param url base URL of the application
     * @return HomePage instance for method chaining
     */
    public HomePage navigateTo(String url) {
        logger.info("Navigating to Periplus homepage: " + url);
        driver.get(url);
        wait.until(ExpectedConditions.titleContains("Periplus"));
        logger.info("Homepage loaded successfully");
        return this;
    }
    
    /**
     * Search for a product
     *
     * @param searchTerm the term to search for
     * @return ProductsPage instance
     */
    public ProductsPage searchForProduct(String searchTerm) {
        logger.info("Searching for product: " + searchTerm);
        enterText(searchBox, searchTerm);
        clickElement(searchButton);
        wait.until(ExpectedConditions.urlContains("filter_name=" + searchTerm.replace(" ", "+")));
        logger.info("Search results displayed");
        return new ProductsPage(driver);
    }
    
    /**
     * Navigate to cart page
     *
     * @return CartPage instance
     */
    public CartPage goToCart() {
        logger.info("Navigating to cart page");
        pageLayout.clickCartIcon();
        waitForPageLoad();
        return new CartPage(driver);
    }
    
    /**
     * Navigate to login page
     *
     * @return LoginPage instance
     */
    public LoginPage goToLoginPage() {
        logger.info("Navigating to login page");
        driver.get("https://www.periplus.com/account/Login");
        return new LoginPage(driver);
    }

    /**
     * Logs out of the website
     */
    public void logout(){
        driver.get("https://www.periplus.com/_index_/Logout");
        waitForPageLoad();
    }
}