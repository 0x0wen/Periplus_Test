package com.openway.tests;

import com.openway.pages.CartPage;
import com.openway.pages.ProductPage;
import com.openway.pages.ProductsPage;
import com.openway.utils.ConfigManager;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test class for cart functionality
 */
public class ShoppingCartTest extends BaseTest {
    
    private static final String BASE_URL = ConfigManager.getProperty("app.url", "https://www.periplus.com/");
    private static final String TEST_EMAIL = ConfigManager.getProperty("test.email");
    private static final String TEST_PASSWORD = ConfigManager.getProperty("test.password");
    private static final String SEARCH_TERM = ConfigManager.getProperty("search.term", "Blockchains");
    
    /**
     * Navigate to home page before each test
     */
    @BeforeMethod
    public void navigateToHomePage() {
        homePage.navigateTo(BASE_URL);
        // Login before each test to ensure we have a consistent state
        homePage.goToLoginPage()
               .login(TEST_EMAIL, TEST_PASSWORD);
    }
    
    /**
     * Clear the cart after each test to ensure a clean state for the next test
     */
    @AfterMethod
    public void clearCart() {
        try {
            CartPage cartPage = homePage.goToCart();
            if (cartPage.hasItems()) {
                cartPage.clearCart();
                Assert.assertTrue(cartPage.isEmpty(), "Cart should be empty after clearing");
            }
            logger.info("Cart cleared successfully after test");
        } catch (Exception e) {
            logger.warning("Failed to clear cart after test: " + e.getMessage());
        }
    }
    
    /**
     * Data provider for different cart testing scenarios
     * @return Object array containing test data for cart operations
     */
    @DataProvider(name = "cartTestData")
    public Object[][] getCartTestData() {
        return new Object[][] {
            {"Blockchain", 0, 1, true},         // Choosing the first product with the quantity of 1
            {"Java Programming", 0, 3, true},   // Choosing the first product with the quantity of 3
            {"Design Patterns", 1, 2, true}     // Choosing the second product with the quantity of 2
        };
    }
    
    /**
     * Test adding products to cart with various scenarios using data provider
     * @param searchTerm The term to search for
     * @param productIndex The index of the product to select from search results
     * @param quantity The quantity to add to cart
     * @param expectedResult The expected result of the add to cart operation
     */
    @Test(description = "Add products to cart with different scenarios", dataProvider = "cartTestData")
    public void testAddProductsToCart(String searchTerm, int productIndex, int quantity, boolean expectedResult) {
        try {
            ProductsPage productsPage = homePage.searchForProduct(searchTerm);
            
            ProductPage productPage = productsPage.selectProductByIndex(productIndex);
            
            String productId = driver.getCurrentUrl().replaceAll(".*/p/([0-9]+).*", "$1");
            double productPrice = productPage.getProductPrice();
            
            if (quantity > 1) {
                productPage.setQuantity(quantity);
            }
            
            productPage.addToCart();
            CartPage cartPage = productPage.goToCart();
            
            Assert.assertTrue(cartPage.hasItems(), "Cart should not be empty");
            
            Assert.assertTrue(cartPage.containsProduct(productId), 
                              "Cart should contain product: " + productId);
            
            Assert.assertEquals(cartPage.getProductQuantity(productId), quantity, 
                               "Product quantity should match the added amount");
            
            double expectedSubtotal = productPrice * quantity;
            Assert.assertEquals(cartPage.getProductSubtotal(productId), expectedSubtotal, 0.01, 
                               "Product subtotal should be price * quantity");
            
            logger.info("Test completed successfully");
        } catch (Exception e) {
            logger.severe("Test failed with exception: " + e.getMessage());
            throw e;  
        }
    }
    
    /**
     * DataProvider for testing multiple products in a single cart session
     */
    @DataProvider(name = "multipleProductsData")
    public Object[][] getMultipleProductsData() {
        return new Object[][] {
            {
                Arrays.asList("Java Programming", "Python", "Data Science"),
                Arrays.asList(0, 1, 0),
                Arrays.asList(1, 2, 3)
            }
        };
    }
    
    /**
     * Test adding multiple different products to cart in a single session
     */
    @Test(description = "Add multiple different products to cart", dataProvider = "multipleProductsData")
    public void testAddMultipleProductsToCart(List<String> searchTerms, List<Integer> productIndices, List<Integer> quantities) {
        logger.info("Starting test: Add multiple products to cart");
        
        try {
            Map<String, Double> productDetails = new HashMap<>();
            Map<String, Integer> productQuantities = new HashMap<>();
            
            for (int i = 0; i < searchTerms.size(); i++) {
                String searchTerm = searchTerms.get(i);
                int productIndex = productIndices.get(i);
                int quantity = quantities.get(i);
                logger.info("Adding: searchTerm: " + searchTerm + ", productIndex: " + productIndex + ", quantity: " + quantity);
                
                ProductsPage productsPage = homePage.searchForProduct(searchTerm);
                ProductPage productPage = productsPage.selectProductByIndex(productIndex);
                
                String productId = driver.getCurrentUrl().replaceAll(".*/p/([0-9]+).*", "$1");
                double productPrice = productPage.getProductPrice();
                
                productDetails.put(productId, productPrice);
                productQuantities.put(productId, quantity);
                
                if (quantity > 1) {
                    productPage.setQuantity(quantity);
                }
                productPage.addToCart();
                
                homePage.navigateTo(BASE_URL);
            }
            
            CartPage cartPage = homePage.goToCart();
            
            Assert.assertTrue(cartPage.hasItems(), "Cart should not be empty");
            
            double expectedTotal = 0.0;
            for (String productId : productDetails.keySet()) {
                double price = productDetails.get(productId);
                int quantity = productQuantities.get(productId);
                
                Assert.assertTrue(cartPage.containsProduct(productId), 
                                 "Cart should contain product: " + productId);
                
                Assert.assertEquals(cartPage.getProductQuantity(productId), quantity, 
                                   "Product quantity should match for: " + productId);
                
                double expectedSubtotal = price * quantity;
                Assert.assertEquals(cartPage.getProductSubtotal(productId), expectedSubtotal, 0.01, 
                                   "Product subtotal should be correct for: " + productId);
                
                expectedTotal += expectedSubtotal;
            }
            
            Assert.assertEquals(cartPage.getCartTotal(), expectedTotal, 0.01, 
                               "Cart total should match sum of all product subtotals");
            
            logger.info("Multiple products test completed successfully");
        } catch (Exception e) {
            logger.severe("Test failed with exception: " + e.getMessage());
            throw e;  
        }
    }

    /**
     * Test removing a product from cart and verifying it was successfully removed
     */
    @Test(description = "Remove product from cart and verify it was successfully removed")
    public void testRemoveFromCart() {
        logger.info("Starting test: Remove product from cart");
        
        try {
            ProductsPage productsPage = homePage.searchForProduct(SEARCH_TERM);
            ProductPage productPage = productsPage.selectFirstProduct();
            
            String productId = driver.getCurrentUrl().replaceAll(".*/p/([0-9]+).*", "$1");

            productPage.addToCart();
            
            CartPage cartPage = productPage.goToCart();
            Assert.assertTrue(cartPage.hasItems(), "Cart should not be empty");
            Assert.assertTrue(cartPage.containsProduct(productId), "Cart should contain the added product");
            
            int initialProductCount = cartPage.getNumberOfProducts();
            
            cartPage.removeProduct(productId);
            
            Assert.assertFalse(cartPage.containsProduct(productId), 
                              "Product should be removed from cart: " + productId);
            
            int expectedRemainingProducts = initialProductCount - 1;
            Assert.assertEquals(cartPage.getNumberOfProducts(), expectedRemainingProducts, 
                                "Number of products should decrease by 1");
            
            if (expectedRemainingProducts == 0) {
                Assert.assertTrue(cartPage.isEmpty(), "Cart should be empty after removing all products");
                Assert.assertTrue(cartPage.isEmptyMessageDisplayed(), "Empty cart message should be displayed");
            }
            
            logger.info("Test completed successfully");
        } catch (Exception e) {
            logger.severe("Test failed with exception: " + e.getMessage());
            throw e;  
        }
    }

    /**
     * Test increasing the quantity of a product in the cart and verifying related fields changes
     */
    @Test(description = "Increase product quantity in cart and verify field updates")
    public void testIncreaseQuantityInCart() {
        logger.info("Starting test: Increase product quantity in cart");
        
        try {
            ProductsPage productsPage = homePage.searchForProduct(SEARCH_TERM);
            ProductPage productPage = productsPage.selectFirstProduct();
            
            String productId = driver.getCurrentUrl().replaceAll(".*/p/([0-9]+).*", "$1");
            double productPrice = productPage.getProductPrice();
            
            productPage.addToCart();
            
            CartPage cartPage = productPage.goToCart();
            
            Assert.assertTrue(cartPage.hasItems(), "Cart should not be empty");
            Assert.assertEquals(cartPage.getProductQuantity(productId), 1, "Initial quantity should be 1");
            double initialTotal = cartPage.getCartTotal();
            
            int initialQuantity = cartPage.getProductQuantity(productId);
            
            int quantityIncrease = 2;
            int newQuantity = initialQuantity + quantityIncrease;
            cartPage.updateProductQuantity(productId, newQuantity);

            cartPage.updateCartPage();
            

            Assert.assertEquals(cartPage.getProductQuantity(productId), newQuantity, 
                               "Quantity should be increased to " + newQuantity);
            
            double expectedNewSubtotal = productPrice * newQuantity;
            Assert.assertEquals(cartPage.getProductSubtotal(productId), expectedNewSubtotal, 0.01, 
                               "Subtotal should reflect the increased quantity");
            
            double expectedTotalIncrease = productPrice * quantityIncrease;
            double expectedNewTotal = initialTotal + expectedTotalIncrease;
            Assert.assertEquals(cartPage.getCartTotal(), expectedNewTotal, 0.01, 
                               "Cart total should reflect the increased product quantity");
            
            logger.info("Test completed successfully");
        } catch (Exception e) {
            logger.severe("Test failed with exception: " + e.getMessage());
            throw e;  
        }
    }

    /**
     * Test decreasing the quantity of a product in the cart and verifying related fields changes
     */
    @Test(description = "Decrease product quantity in cart and verify field updates")
    public void testDecreaseQuantityInCart() {
        logger.info("Starting test: Decrease product quantity in cart");
        
        try {
            ProductsPage productsPage = homePage.searchForProduct(SEARCH_TERM);
            ProductPage productPage = productsPage.selectFirstProduct();
            
            String productId = driver.getCurrentUrl().replaceAll(".*/p/([0-9]+).*", "$1");
            double productPrice = productPage.getProductPrice();
            
            productPage.setQuantity(3);
            productPage.addToCart();
            
            CartPage cartPage = productPage.goToCart();
            
            Assert.assertTrue(cartPage.hasItems(), "Cart should not be empty");
            Assert.assertEquals(cartPage.getProductQuantity(productId), 3, "Initial quantity should be 3");
            
            int initialQuantity = cartPage.getProductQuantity(productId);
            double initialTotal = cartPage.getCartTotal();
            
            int quantityDecrease = 1;
            int newQuantity = initialQuantity - quantityDecrease;
            cartPage.updateProductQuantity(productId, newQuantity);
            
            cartPage.updateCartPage();
            
            Assert.assertEquals(cartPage.getProductQuantity(productId), newQuantity, 
                               "Quantity should be decreased to " + newQuantity);
            
            double expectedNewSubtotal = productPrice * newQuantity;
            Assert.assertEquals(cartPage.getProductSubtotal(productId), expectedNewSubtotal, 0.01, 
                               "Subtotal should reflect the decreased quantity");
            
            double expectedTotalDecrease = productPrice * quantityDecrease;
            double expectedNewTotal = initialTotal - expectedTotalDecrease;
            Assert.assertEquals(cartPage.getCartTotal(), expectedNewTotal, 0.01, 
                               "Cart total should reflect the decreased product quantity");
            
            logger.info("Test completed successfully");
        } catch (Exception e) {
            logger.severe("Test failed with exception: " + e.getMessage());
            throw e;  
        }
    }
    
    /**
     * Test cart persistence between sessions
     */
    @Test(description = "Verify cart persists between sessions")
    public void testCartPersistence() {
        logger.info("Starting test: Cart persistence between sessions");
        
        try {
            ProductsPage productsPage = homePage.searchForProduct(SEARCH_TERM);
            ProductPage productPage = productsPage.selectFirstProduct();
            
            String productId = driver.getCurrentUrl().replaceAll(".*/p/([0-9]+).*", "$1");
            productPage.addToCart();
            
            CartPage cartPage = productPage.goToCart();
            Assert.assertTrue(cartPage.containsProduct(productId), "Cart should contain the added product");
            
            int quantity = cartPage.getProductQuantity(productId);
            double subtotal = cartPage.getProductSubtotal(productId);
            
            homePage.logout();
            homePage.goToLoginPage()
                   .login(TEST_EMAIL, TEST_PASSWORD);
            
            cartPage = homePage.goToCart();
            
            Assert.assertTrue(cartPage.containsProduct(productId), 
                             "Cart should still contain the product after session restart");
            Assert.assertEquals(cartPage.getProductQuantity(productId), quantity, 
                               "Product quantity should persist between sessions");
            Assert.assertEquals(cartPage.getProductSubtotal(productId), subtotal, 0.01, 
                               "Product subtotal should persist between sessions");
            
            logger.info("Test completed successfully");
        } catch (Exception e) {
            logger.severe("Test failed with exception: " + e.getMessage());
            throw e;  
        }
    }
}