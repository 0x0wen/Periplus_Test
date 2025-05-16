package com.openway.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends BasePage{
    @FindBy(name = "email")
    private WebElement emailField;
    @FindBy(name = "password")
    private WebElement passwordField;
    @FindBy(id = "button-login")
    private WebElement loginButton;
    @FindBy(className = "warning")
    private WebElement errorMessage;

    /**
     * Constructor
     *
     * @param driver WebDriver instance
     */
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void setEmail(String email){
        enterText(emailField, email);
    }

    public void setPassword(String password){
        enterText(passwordField, password);
    }

    public ProductsPage clickLoginButton(){
        clickElement(loginButton);
        return new ProductsPage(driver);
    }

    /**
     * Login with email and password
     *
     * @param email user email
     * @param password user password
     * @return HomePage instance after successful login
     */
    public HomePage login(String email, String password) {
        logger.info("Performing login with email: " + email);
        
        waitForVisibility(emailField);
        
        setEmail(email);
        setPassword(password);
        clickLoginButton();

        return new HomePage(driver);
    }

    public String getErrorMessage(){
        return errorMessage.getText();
    }
}
