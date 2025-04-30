# Periplus Shopping Cart Test Cases

## How to run?
```bash
    mvn clean test -Dtest.email=your@email.com -Dtest.password=your_password
```

## TC-CART-001: Add a product to the cart

**Objective:**  
Ensure product addition works correctly

**Preconditions:**  
- The user is registered and logged in  
- Has an internet connection  
- Cart is empty

**Data:**  
- Product in "Blockchains"

**Test Steps:**
1. Opens Google Chrome in a new window  
2. Navigates to https://www.periplus.com/  
3. Enters a login and password  
4. Finds one product in "Blockchains"  
5. Adds the first product to the cart  
6. Verifies that the product has been successfully added to the cart

**Expected Result:**  
- The cart has a product from “Blockchains”