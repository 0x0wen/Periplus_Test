package com.openway.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class CartPage extends BasePage {
    
    @FindBy(xpath = "//div[contains(@class,'shopping-summery')]")
    private WebElement shoppingSummary;
    
    @FindBy(xpath = "//li[contains(text(),'Sub-Total')]/span[@id='sub_total']")
    private WebElement subTotal;
    
    @FindBy(xpath = "//li[contains(text(),'Total')]/span[@id='sub_total']")
    private WebElement total;

    @FindBy(xpath = "//div[@class='content' and contains(text(), 'Your shopping cart is empty')]")
    private WebElement emptyCartMessage;

    @FindBy(xpath = "//div[@class='button4']//input")
    private WebElement updateButton;
    
    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public CartPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Check if shopping summary is displayed
     * 
     * @return true if shopping summary is displayed, false otherwise
     */
    public boolean isShoppingSummaryDisplayed() {
        return isElementPresent(By.xpath("//div[contains(@class,'shopping-summery')]"));
    }
    
    /**
     * Get the subtotal value from cart
     * 
     * @return subtotal as integer
     */
    public int getSubTotal() {
        String subTotalText = subTotal.getText().replaceAll("[^0-9]", "");
        return Integer.parseInt(subTotalText);
    }
    
    /**
     * Get the total value from cart
     * 
     * @return total as integer
     */
    public int getTotal() {
        String totalText = total.getText().replaceAll("[^0-9]", "");
        return Integer.parseInt(totalText);
    }
    
    /**
     * Get the cart total as double
     * 
     * @return cart total as double
     */
    public double getCartTotal() {
        String totalText = total.getText().replaceAll("[^0-9.]", "");
        return Double.parseDouble(totalText);
    }

    /**
     * Update the cart page
     *
     * @return CartPage instance for method chaining
     */
    public CartPage updateCartPage(){
        clickElement(updateButton);
        waitForPageLoad();
        return new CartPage(driver);
    }

    /**
     * Verify that the cart is not empty
     *
     * @return true if cart has items, false otherwise
     */
    public boolean hasItems() {
        logger.info("Verifying cart has items");
        
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[contains(@class,'shopping-summery') or contains(@class,'shopping-cart')] | //h1")));
        
        boolean hasItems = !driver.findElements(
                By.xpath("//div[contains(@class,'row-cart-product')]")).isEmpty();
        
        if (hasItems) {
            logger.info("Cart has items");
        } else {
            logger.warning("Cart is empty");
        }
        
        return hasItems;
    }
    
    /**
     * Check if cart is empty
     * 
     * @return true if cart is empty, false otherwise
     */
    public boolean isEmpty() {
        return !hasItems();
    }
    
    /**
     * Check if empty cart message is displayed
     * 
     * @return true if empty cart message is displayed, false otherwise
     */
    public boolean isEmptyMessageDisplayed() {
        return emptyCartMessage.isDisplayed();
    }
    
    /**
     * Get all product IDs in the cart
     * 
     * @return List of product IDs
     */
    public List<String> getProductIds() {
        List<String> productIds = new ArrayList<>();
        List<WebElement> productElements = driver.findElements(By.xpath("//div[contains(@class,'row-cart-product')]"));
        
        for (WebElement productElement : productElements) {
            String productId = productElement.getText().replaceAll("\\s+", " ").trim();
            if (productId.matches(".*\\d{13}.*")) {
                productId = productId.replaceAll(".*?(\\d{13}).*", "$1").trim();
                productIds.add(productId);
            }
        }
        
        return productIds;
    }
    
    /**
     * Verify that the product with the given ID is in the cart
     *
     * @param productId the ID of the product to check for
     * @return true if product is in cart, false otherwise
     */
    public boolean isProductInCart(String productId) {
        logger.info("Checking if product is in cart with ID: " + productId);
        
        return isElementPresent(By.xpath("//div[contains(@class,'row-cart-product')][.//text()[contains(.,'" + productId + "')]]"));
    }
    
    /**
     * Verify that the product with the given title is in the cart
     *
     * @param productTitle the title of the product to check for
     * @return true if product is in cart, false otherwise
     */
    public boolean isProductTitleInCart(String productTitle) {
        logger.info("Checking if product is in cart with title: " + productTitle);
        
        return isElementPresent(By.xpath(
                "//div[contains(@class,'row-cart-product')]//p[contains(@class,'product-name')][contains(text(),'" + 
                productTitle + "')]"));
    }
    
    /**
     * Alias method for isProductInCart for better readability
     * 
     * @param productId the ID of the product to check for
     * @return true if product is in cart, false otherwise
     */
    public boolean containsProduct(String productId) {
        return isProductInCart(productId);
    }
    
    /**
     * Get the quantity of a product in the cart
     * 
     * @param productId the ID of the product to get quantity for
     * @return quantity of the product
     */
    public int getProductQuantity(String productId) {
        logger.info("Getting quantity for product ID: " + productId);
        
        WebElement productRow = findProductRowById(productId);
        
        if (productRow == null) {
            logger.warning("Product not found in cart with ID: " + productId);
            return 0;
        }
        
        WebElement quantityElement;
        try {
            quantityElement = productRow.findElement(By.xpath(".//div[contains(@class,'row qty')]//input"));
            return Integer.parseInt(quantityElement.getDomAttribute("value"));
        } catch (NoSuchElementException e) {
            try {
                quantityElement = productRow.findElement(By.xpath(".//div[contains(@class,'row qty')]"));
                return Integer.parseInt(quantityElement.getText().trim());
            } catch (NoSuchElementException e2) {
                logger.warning("Could not find quantity element for product ID: " + productId);
                return 0;
            }
        }
    }
    
    /**
     * Get the subtotal for a specific product in cart by multiplying price and quantity
     * 
     * @param productId the ID of the product to get subtotal for
     * @return subtotal as double
     */
    public double getProductSubtotal(String productId) {
        logger.info("Getting subtotal for product ID: " + productId);
        
        WebElement productRow = findProductRowById(productId);
        
        if (productRow == null) {
            logger.warning("Product not found in cart with ID: " + productId);
            return 0.0;
        }
        
        WebElement priceElement = productRow.findElement(By.xpath(".//div[@class='row' and contains(text(), 'Rp')]"));
        String priceText = priceElement.getText().trim().split("or")[0].trim();
        priceText = priceText.replaceAll("[^0-9.,]", "").replace(",", "");
        double price = Double.parseDouble(priceText);
        
        WebElement quantityElement = productRow.findElement(By.xpath(".//div[contains(@class,'row qty')]//input"));
        String quantityText = quantityElement.getDomAttribute("value");
        int quantity = Integer.parseInt(quantityText);
        
        double subtotal = price * quantity;
        
        logger.info("Calculated subtotal for product ID " + productId + ": " + subtotal);
        return subtotal;
    }
    
    /**
     * Update the quantity of a product in the cart using plus and minus buttons
     * 
     * @param productId the ID of the product to update quantity for
     * @param newQuantity the new quantity
     * @return CartPage instance for method chaining
     */
    public CartPage updateProductQuantity(String productId, int newQuantity) {
        logger.info("Updating quantity for product ID: " + productId + " to " + newQuantity);
        
        WebElement productRow = findProductRowById(productId);
        
        if (productRow == null) {
            logger.severe("Product not found in cart with ID: " + productId);
            throw new NoSuchElementException("Product not found in cart with ID: " + productId);
        }
        
        try {
            WebElement quantityInput = productRow.findElement(By.xpath(".//div[contains(@class,'row qty')]//input"));
            int currentQuantity = Integer.parseInt(quantityInput.getDomAttribute("value"));
            
            WebElement minusButton = productRow.findElement(By.xpath(".//button[@data-type='minus' and contains(@name, 'minus')]"));
            WebElement plusButton = productRow.findElement(By.xpath(".//button[@data-type='plus' and contains(@name, 'plus')]"));
            
            if (newQuantity > currentQuantity) {
                int clickCount = newQuantity - currentQuantity;
                for (int i = 0; i < clickCount; i++) {
                    clickElement(plusButton);
                    wait.until(ExpectedConditions.attributeToBe(quantityInput, "value", String.valueOf(currentQuantity + i + 1)));
                }
            } else if (newQuantity < currentQuantity) {
                int clickCount = currentQuantity - newQuantity;
                for (int i = 0; i < clickCount; i++) {
                    clickElement(minusButton);
                    wait.until(ExpectedConditions.attributeToBe(quantityInput, "value", String.valueOf(currentQuantity - i - 1)));
                }
            } else {
                logger.info("Quantity is already set to " + newQuantity + ". No action needed.");
                return this;
            }
            
        } catch (NoSuchElementException e) {
            logger.severe("Could not find quantity controls for product ID: " + productId);
            throw new NoSuchElementException("Could not find quantity controls for product ID: " + productId + ": " + e.getMessage());
        }
        
        waitForPageLoad();
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[contains(@class,'updating-cart')]")));
        } catch (Exception e) {
            logger.info("No updating-cart indicator found or it disappeared quickly");
        }
        
        logger.info("Product quantity updated successfully to " + newQuantity);
        return this;
    }
    
    /**
     * Remove a product from the cart
     * 
     * @param productId the ID of the product to remove
     * @return CartPage instance for method chaining
     */
    public CartPage removeProduct(String productId) {
        logger.info("Removing product from cart with ID: " + productId);
        
        WebElement productRow = findProductRowById(productId);
        
        if (productRow == null) {
            logger.severe("Product not found in cart with ID: " + productId);
            throw new NoSuchElementException("Product not found in cart with ID: " + productId);
        }
        
        WebElement removeButton = productRow.findElement(By.xpath(".//a[contains(@class,'btn-cart-remove')]"));
        clickElement(removeButton);
        
        waitForPageLoad();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(
                "//div[contains(@class,'row-cart-product')][contains(text(),'" + productId + "')]")));
        
        logger.info("Product removed successfully");
        return this;
    }
    
    /**
     * Remove all products from cart
     * 
     * @return CartPage instance for method chaining
     */
    public CartPage clearCart() {
        logger.info("Clearing all products from cart");
        
        List<String> productIds = getProductIds();
        
        if (productIds.isEmpty()) {
            logger.info("Cart is already empty");
            return this;
        }
        
        logger.info("Found " + productIds.size() + " products to remove from cart");
        
        for (String productId : productIds) {
            try {
                logger.info("Removing product with ID: " + productId);
                removeProduct(productId);
                
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.warning("Thread interrupted while waiting between product removals");
                }
            } catch (NoSuchElementException e) {
                logger.warning("Failed to remove product with ID: " + productId + ". Error: " + e.getMessage());
            }
        }
        
        waitForPageLoad();
        if (isEmpty()) {
            logger.info("Cart successfully cleared");
        } else {
            logger.warning("Failed to clear cart completely. " + getNumberOfProducts() + " products remain");
        }
        
        return this;
    }

    /**
     * Get the number of products in the cart
     * 
     * @return number of products
     */
    public int getNumberOfProducts() {
        List<WebElement> productRows = driver.findElements(By.xpath(
                "//div[contains(@class,'row-cart-product')]"));
        return productRows.size();
    }
    
    /**
     * Helper method to find a product row in the cart by ID
     * 
     * @param productId the ID of the product to find
     * @return WebElement of the product row, or null if not found
     */
    private WebElement findProductRowById(String productId) {
        try {
            return driver.findElement(By.xpath("//div[contains(@class,'row-cart-product')][.//text()[contains(.,'" + productId + "')]]"));
        } catch (NoSuchElementException e) {
            return null;
        }
    }
    
    /**
     * Comprehensive method to verify cart contents
     * 
     * @param expectedProductId Expected product ID
     * @param expectedProductTitle Expected product title (partial match)
     * @param expectedQuantity Expected quantity
     * @param expectedTotal Expected total amount
     * @return true if all verification points pass, false otherwise
     */
    public boolean verifyCartItem(String expectedProductId, String expectedProductTitle, 
                                 int expectedQuantity, double expectedTotal) {
        logger.info("Performing comprehensive verification of cart item");
        logger.info("Expected - ID: " + expectedProductId + ", Title: " + expectedProductTitle + 
                   ", Quantity: " + expectedQuantity + ", Total: " + expectedTotal);
        
        boolean idMatches = isProductInCart(expectedProductId);
        boolean titleMatches = isProductTitleInCart(expectedProductTitle);
        int actualQuantity = getProductQuantity(expectedProductId);
        double actualTotal = getProductSubtotal(expectedProductId);
        
        boolean quantityMatches = (actualQuantity == expectedQuantity);
        boolean totalMatches = Math.abs(actualTotal - expectedTotal) < 0.01; // Allow small rounding differences
        
        logger.info("Verification results - ID: " + idMatches + ", Title: " + titleMatches + 
                   ", Quantity: " + quantityMatches + ", Total: " + totalMatches);
        
        return idMatches && titleMatches && quantityMatches && totalMatches;
    }
}