package com.openway.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.logging.Logger;

public class AppTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private final Logger logger = Logger.getLogger(AppTest.class.getName());

    // Test data - should be moved to a configuration file in a real project
    private static final String PERIPLUS_URL = "https://www.periplus.com/";
    private static final String TEST_EMAIL = System.getProperty("test.email", System.getenv("TEST_EMAIL"));  // Replace with your actual test account
    private static final String TEST_PASSWORD = System.getProperty("test.password", System.getenv("TEST_PASSWORD"));  // Replace with your actual password
    private static final String SEARCH_TERM = "Blockchains";

    @BeforeClass
    public void setUp() {
        logger.info("Setting up WebDriver and test environment");

        // Setup WebDriverManager to handle driver binaries
        WebDriverManager.chromedriver().setup();

        // Configure Chrome options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");  // Start browser maximized
        options.addArguments("--disable-notifications");  // Disable notifications

        // Initialize Chrome driver with options
        driver = new ChromeDriver(options);

        // Initialize WebDriverWait with 10 seconds timeout
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        logger.info("WebDriver setup complete");
    }

    @BeforeMethod
    public void navigateToHomePage() {
        logger.info("Navigating to Periplus homepage");
        driver.get(PERIPLUS_URL);

        // Wait for homepage to load completely
        wait.until(ExpectedConditions.titleContains("Periplus"));
        logger.info("Homepage loaded successfully");
    }

    @Test(description = "Add product to cart and verify it was successfully added")
    public void testAddToCart() {
        try {
            // Step 1: Login to account
            login(TEST_EMAIL, TEST_PASSWORD);

            // Step 2: Find a product
            searchForProduct(SEARCH_TERM);

            // Step 3: Select a product from search results
            selectFirstProductFromResults();

            // Step 4: Add product to cart
            addCurrentProductToCart();

            // Step 5: Verify product was added to cart
            verifyProductAddedToCart();

            logger.info("Test completed successfully");
        } catch (Exception e) {
            logger.severe("Test failed with exception: " + e.getMessage());
            Assert.fail("Test failed: " + e.getMessage(), e);
        }
    }

    /**
     * Logs in to Periplus with the provided credentials
     */
    private void login(String email, String password) {
        logger.info("Performing login");

        driver.get("https://www.periplus.com/account/Login");

        // Wait for login form to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("email")));

        // Enter email
        WebElement emailField = driver.findElement(By.name("email"));
        emailField.clear();
        emailField.sendKeys(email);

        // Enter password
        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.clear();
        passwordField.sendKeys(password);

        // Click login button
        WebElement loginButton = driver.findElement(By.id("button-login"));
        loginButton.click();

        // Wait for successful login (check for presence of account element or absence of login form)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("email")));

        logger.info("Login successful");
    }

    /**
     * Searches for a product using the search box
     */
    private void searchForProduct(String searchTerm) {
        logger.info("Searching for product: " + searchTerm);

        // Find search box
        WebElement searchBox = driver.findElement(By.id("filter_name"));
        searchBox.clear();
        searchBox.sendKeys(searchTerm);

        // Click search button or press Enter
        WebElement searchBar = driver.findElement(By.className("search-bar"));
        WebElement searchButton = searchBar.findElement(By.cssSelector("button[type='submit']"));
        searchButton.click();

        wait.until(ExpectedConditions.urlContains("filter_name=" + searchTerm.replace(" ", "+")));

        logger.info("Search results displayed");
    }

    /**
     * Selects the first product from search results
     */
    private void selectFirstProductFromResults() {
        logger.info("Selecting first product from search results");

        // Find and click the first product in search results
        WebElement firstProduct = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[contains(@class,'single-product')]//a)[1]")));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
        // Click on the product
        firstProduct.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));
        // Wait for product page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'row-product-detail')]")));

        logger.info("Product page loaded");
    }

    /**
     * Adds the current product to cart
     */
    private void addCurrentProductToCart() {
        logger.info("Adding product to cart");

        WebElement h2 = driver.findElement(By.tagName("h2"));
        String bookTitle = h2.getText();
        logger.info("Adding the book: " + bookTitle);
        // Find and click "Add to Cart" button
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Add to Cart') or contains(@class,'add-to-cart')]")));
        addToCartButton.click();

        // Wait for confirmation message or cart update
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class,'Success add to cart') or contains(@class,'notification')]")),
                ExpectedConditions.textToBePresentInElementLocated(
                        By.xpath("//a[contains(@class,'cart-icon')]//span"), "1")
        ));
//        WebElement closeModalButton = driver.findElement(By.className("btn-modal-close"));;
//        closeModalButton.click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class,'Success add to cart') or contains(@class,'notification')]")));

        logger.info("Product added to cart");
    }

    /**
     * Verifies that product was successfully added to cart
     */
    private void verifyProductAddedToCart() {
        logger.info("Verifying product was added to cart");

        // Navigate to cart page
        WebElement cartIcon = driver.findElement(By.id("show-your-cart"));;
        cartIcon.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("preloader")));

        // Wait for cart page to load
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'shopping-summery') or contains(@class,'shopping-cart')]")));

        // Verify cart is not empty
        boolean cartHasItems = driver.findElements(
                By.xpath("//div[contains(@class,'row-cart-product') or contains(@class,'line-item')]")).size() > 0;

        Assert.assertTrue(cartHasItems, "Cart should not be empty");

        logger.info("Product successfully verified in cart");
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