package com.openway.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class ProductsPage extends BasePage {

    @FindBy(className = "product-area")
    private WebElement productArea;
    
    @FindBy(xpath = "(//div[contains(@class,'single-product')]//a)[1]")
    private WebElement firstProduct;
    
    @FindBy(xpath = "//div[contains(@class,'single-product')]//a")
    private List<WebElement> productLinks;

    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Check if product area is displayed
     * 
     * @return true if product area is displayed, false otherwise
     */
    public boolean isProductAreaDisplayed() {
        return productArea.isDisplayed();
    }

    /**
     * Select the first product from search results
     *
     * @return ProductPage instance
     */
    public ProductPage selectFirstProduct() {
        logger.info("Selecting first product from search results");
        
        waitForPageLoad();
        waitForClickability(firstProduct);
        
        clickElement(firstProduct);
        
        waitForPageLoad();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'row-product-detail')]")));
        
        logger.info("Product page loaded");
        return new ProductPage(driver);
    }
    
    /**
     * Select a product from search results by index
     *
     * @param index the index of the product to select (0-based)
     * @return ProductPage instance
     */
    public ProductPage selectProductByIndex(int index) {
        logger.info("Selecting product at index " + index + " from search results");
        
        waitForPageLoad();
        
        if (productLinks.size() <= index) {
            logger.severe("Product index out of bounds. Requested: " + index + ", Available: " + productLinks.size());
            throw new IndexOutOfBoundsException("Product index out of bounds: " + index);
        }
        
        WebElement productLink = productLinks.get(index);
        waitForClickability(productLink);
        clickElement(productLink);
        
        waitForPageLoad();
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class,'row-product-detail')]")));
        
        logger.info("Product page loaded");
        return new ProductPage(driver);
    }
    
    /**
     * Get the number of products displayed in search results
     *
     * @return number of products
     */
    public int getNumberOfProducts() {
        return productLinks.size();
    }
}