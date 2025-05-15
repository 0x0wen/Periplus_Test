package com.openway.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ProductPage extends BasePage {

    @FindBy(tagName = "h2")
    private WebElement productTitle;
    
    @FindBy(xpath = "//div[@class='quickview-price']//span")
    private WebElement price;
    
    @FindBy(xpath = "//button[contains(@class,'btn-add-to-cart')]")
    private WebElement addToCartButton;
    
    @FindBy(xpath = "//button[contains(@class,'btn-product-plus')]")
    private WebElement incrementQuantityButton;

    @FindBy(xpath = "//button[contains(@class,'btn-product-minus')]")
    private WebElement decrementQuantityButton;

    @FindBy(id = "show-your-cart")
    private WebElement cartIcon;
    
    @FindBy(xpath = "//button[@data-type='minus']/following-sibling::input[contains(@class, 'input-number')]")
    private WebElement quantityInput;

    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public ProductPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Get the price of the product as an integer
     *
     * @return product price as integer
     */
    public int getPrice() {
        String priceText = price.getText().replaceAll("[^0-9]", "");
        return Integer.parseInt(priceText);
    }
    
    /**
     * Get the price of the product as a double
     *
     * @return product price as double
     */
    public double getProductPrice() {
        String priceText = price.getText().replaceAll("[^0-9.]", "");
        return Double.parseDouble(priceText);
    }

    /**
     * Get the title of the product
     *
     * @return product title text
     */
    public String getProductTitle() {
        waitForVisibility(productTitle);
        return productTitle.getText();
    }

    /**
     * Get the quantity of the product
     *
     * @return product quantity
     */
    public int getCurrentQuantity(){
        waitForVisibility(quantityInput);
        return Integer.parseInt(quantityInput.getDomAttribute("value"));
    }

    /**
     * Click the Add to Cart button
     */
    public void clickAddToCartButton() {
        clickElement(addToCartButton);
    }

    /**
     * Set the quantity of the product to add to cart
     * 
     * @param quantity quantity to set
     * @return ProductPage instance for method chaining
     */
    public ProductPage setQuantity(int quantity) {
        logger.info("Setting product quantity to: " + quantity);
        int difference = quantity - getCurrentQuantity();
        if(difference < 0){
            for(int i = 0; i > difference; i--){
                clickElement(decrementQuantityButton);
            }
        }else if(difference > 0){
            for(int i = 0; i < difference; i++){
                clickElement(incrementQuantityButton);
            }
        }

        logger.info("Quantity set successfully");
        return this;
    }

    /**
     * Add current product to cart
     *
     * @return ProductPage instance for method chaining
     */
    public ProductPage addToCart() {
        String bookTitle = getProductTitle();
        logger.info("Adding the book to cart: " + bookTitle);
        
        clickElement(addToCartButton);
        
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[contains(@class,'Success add to cart') or contains(@class,'notification')]")),
                ExpectedConditions.textToBePresentInElementLocated(
                        By.xpath("//a[contains(@class,'cart-icon')]//span"), "1")
        ));
        
        if (isElementPresent(By.xpath("//div[contains(@class,'Success add to cart') or contains(@class,'notification')]"))) {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(
                    By.xpath("//div[contains(@class,'Success add to cart') or contains(@class,'notification')]")));
        }
        
        logger.info("Product added to cart");
        return this;
    }
    
    /**
     * Navigate to cart page
     *
     * @return CartPage instance
     */
    public CartPage goToCart() {
        logger.info("Navigating to cart page");
        clickElement(cartIcon);
        waitForPageLoad();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'shopping-summery') or contains(@class,'shopping-cart')]")));
        logger.info("Cart page loaded");
        return new CartPage(driver);
    }
}