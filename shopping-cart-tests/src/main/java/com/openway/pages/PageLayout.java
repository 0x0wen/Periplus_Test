package com.openway.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PageLayout extends BasePage{
    @FindBy(xpath = "//span[@id='nav-signin-text']//a")
    private WebElement signInButton;

    @FindBy(xpath = "filter_name")
    private WebElement searchBar;

    @FindBy(xpath = "//div[@class='search-bar']//button[@type='submit']")
    private WebElement searchButton;

    @FindBy(xpath = "//div[contains(@class,'sinlge-bar shopping')]//a[@class='single-icon']")
    private WebElement yourAccountButton;

    @FindBy(id = "show-your-cart")
    private WebElement cartIcon;

    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public PageLayout(WebDriver driver) {
        super(driver);
    }

    public boolean isUserSignedIn(){
        return signInButton.isDisplayed();
    }

    public void setSearchTerm(String text){
        enterText(searchBar, text);
    }

    public ProductsPage clickSearchButton(){
        clickElement(searchButton);
        return new ProductsPage(driver);
    }

    public CartPage clickCartIcon(){
        clickElement(cartIcon);
        return new CartPage(driver);
    }
}
