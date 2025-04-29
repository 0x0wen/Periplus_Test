# Periplus Shopping Cart Test Cases

## Basic Cart Operations

### TC-CART-001: Add Single Item to Empty Cart
**Preconditions:** 
- User is logged into Periplus account
- Shopping cart is empty
- Product catalog is accessible

**Test Steps:**
1. Navigate to Periplus homepage
2. Search for a book title
3. Click on a product from search results
4. Click "Add to Cart" button

**Expected Results:**
1. Homepage is displayed
2. Search results display relevant books
3. Product details page opens
4. Product is added to cart, cart icon shows "1" item, confirmation message appears

### TC-CART-002: Add Multiple Quantities of Same Item
**Preconditions:** 
- User is logged into Periplus account
- Shopping cart is empty
- Product catalog is accessible

**Test Steps:**
1. Navigate to product details page
2. Set quantity to more than 1 (e.g., 3)
3. Click "Add to Cart" button

**Expected Results:**
1. Product details page opens
2. Quantity field accepts the new value
3. Product is added to cart with selected quantity, cart icon shows correct quantity

### TC-CART-003: Add Multiple Different Items to Cart
**Preconditions:** 
- User is logged into Periplus account
- Shopping cart is empty
- Product catalog is accessible

**Test Steps:**
1. Add first item to cart
2. Navigate back to catalog/search results
3. Select a different product
4. Add second item to cart

**Expected Results:**
1. First item is added successfully
2. Returns to catalog/search results
3. Second product details page opens
4. Second item is added to cart, cart icon shows "2" items

### TC-CART-004: View Cart Contents
**Preconditions:** 
- User is logged into Periplus account
- At least one item in shopping cart

**Test Steps:**
1. Click on cart icon/button
2. Review cart contents page

**Expected Results:**
1. Cart page opens
2. All added items are displayed with correct title, quantity, and price
3. Subtotal is calculated correctly

### TC-CART-005: Update Item Quantity in Cart
**Preconditions:** 
- User is logged into Periplus account
- At least one item in shopping cart

**Test Steps:**
1. Navigate to cart page
2. Locate quantity field for an item
3. Change quantity (increase and decrease)
4. Click update button (if applicable)

**Expected Results:**
1. Cart page opens
2. Quantity field is visible
3. Quantity changes are accepted
4. Cart updates with new quantities and recalculates prices

### TC-CART-006: Remove Item from Cart
**Preconditions:** 
- User is logged into Periplus account
- At least one item in shopping cart

**Test Steps:**
1. Navigate to cart page
2. Locate remove/delete button for an item
3. Click the remove button
4. Confirm removal (if prompted)

**Expected Results:**
1. Cart page opens
2. Remove button is visible
3. Confirmation prompt appears (if applicable)
4. Item is removed from cart, subtotal recalculated

### TC-CART-007: Empty Entire Cart
**Preconditions:** 
- User is logged into Periplus account
- Multiple items in shopping cart

**Test Steps:**
1. Navigate to cart page
2. Locate "Empty Cart" or similar button (if available)
3. Click the button
4. Confirm emptying cart (if prompted)

**Expected Results:**
1. Cart page opens
2. Empty cart option is visible
3. Confirmation prompt appears
4. All items are removed from cart, cart shows empty state

## Boundary & Edge Cases

### TC-CART-008: Add Maximum Allowed Quantity
**Preconditions:** 
- User is logged into Periplus account
- Shopping cart is empty
- Product catalog is accessible

**Test Steps:**
1. Navigate to product details page
2. Set quantity to maximum allowed value
3. Click "Add to Cart" button

**Expected Results:**
1. Product details page opens
2. Maximum quantity is accepted
3. Product is added to cart with maximum quantity, cart updates correctly

### TC-CART-009: Exceed Maximum Allowed Quantity
**Preconditions:** 
- User is logged into Periplus account
- Shopping cart is empty
- Product catalog is accessible

**Test Steps:**
1. Navigate to product details page
2. Try to set quantity above maximum allowed
3. Click "Add to Cart" button

**Expected Results:**
1. Product details page opens
2. System displays error message or automatically adjusts to maximum allowed quantity
3. Only permitted quantity is added to cart

### TC-CART-010: Add Out-of-Stock Item
**Preconditions:** 
- User is logged into Periplus account
- Locate an out-of-stock product

**Test Steps:**
1. Navigate to out-of-stock product page
2. Observe "Add to Cart" button state
3. Attempt to add item to cart if button is enabled

**Expected Results:**
1. Product details page shows "Out of Stock" status
2. "Add to Cart" button is disabled or shows alternative action
3. System prevents adding out-of-stock items or provides pre-order option

### TC-CART-011: Add Item with Zero Quantity
**Preconditions:** 
- User is logged into Periplus account
- Shopping cart is empty
- Product catalog is accessible

**Test Steps:**
1. Navigate to product details page
2. Set quantity to 0
3. Click "Add to Cart" button

**Expected Results:**
1. Product details page opens
2. System prevents setting quantity to 0 or displays error message
3. Item is not added to cart

## Cart Session & Persistence

### TC-CART-012: Cart Persistence After Logout/Login
**Preconditions:** 
- User is logged into Periplus account
- Items added to shopping cart

**Test Steps:**
1. Add items to cart
2. Log out from account
3. Log back in to the account

**Expected Results:**
1. Items are added successfully
2. Logout successful
3. After login, cart items remain in the cart

### TC-CART-013: Cart State Across Different Devices/Browsers
**Preconditions:** 
- User has access to multiple devices/browsers
- User account credentials

**Test Steps:**
1. Login on first device/browser
2. Add items to cart
3. Login on second device/browser
4. Check cart contents

**Expected Results:**
1. Login successful on first device
2. Items added to cart
3. Login successful on second device
4. Cart shows same items on both devices

### TC-CART-014: Cart Expiration (if applicable)
**Preconditions:** 
- User is logged into Periplus account
- Items added to shopping cart
- Knowledge of cart expiration timeframe

**Test Steps:**
1. Add items to cart
2. Wait for cart expiration period
3. Refresh page or revisit cart

**Expected Results:**
1. Items are added successfully
2. Time passes
3. System behavior follows expected expiration policy (keeps items or expires them)

## Integration Cases

### TC-CART-015: Proceed to Checkout from Cart
**Preconditions:** 
- User is logged into Periplus account
- Items added to shopping cart

**Test Steps:**
1. Navigate to cart page
2. Click "Proceed to Checkout" or similar button
3. Observe transition to checkout page

**Expected Results:**
1. Cart page opens with items
2. Checkout button is enabled
3. System navigates to checkout page with cart items preserved

### TC-CART-016: Continue Shopping from Cart Page
**Preconditions:** 
- User is logged into Periplus account
- Items added to shopping cart
- User is on cart page

**Test Steps:**
1. Click "Continue Shopping" or similar button
2. Navigate back to cart after browsing

**Expected Results:**
1. System returns to catalog or homepage
2. Cart items remain when returning to cart page

### TC-CART-017: Apply Coupon/Promo Code in Cart
**Preconditions:** 
- User is logged into Periplus account
- Items added to shopping cart
- Valid coupon code available

**Test Steps:**
1. Navigate to cart page
2. Locate coupon code field
3. Enter valid coupon code
4. Apply the coupon

**Expected Results:**
1. Cart page opens
2. Coupon field is accessible
3. System accepts the coupon code
4. Discount is applied to order total

### TC-CART-018: Apply Invalid Coupon/Promo Code
**Preconditions:** 
- User is logged into Periplus account
- Items added to shopping cart

**Test Steps:**
1. Navigate to cart page
2. Locate coupon code field
3. Enter invalid coupon code
4. Apply the coupon

**Expected Results:**
1. Cart page opens
2. Coupon field is accessible
3. System rejects invalid code
4. Error message displayed, no discount applied

## Performance & Error Cases

### TC-CART-019: Add Items Rapidly to Cart
**Preconditions:** 
- User is logged into Periplus account
- Shopping cart is empty
- Product catalog is accessible

**Test Steps:**
1. Quickly add multiple items to cart in succession
2. Check cart contents

**Expected Results:**
1. All items are added correctly
2. Cart shows accurate item count and contents

### TC-CART-020: Edit Cart with Network Interruption
**Preconditions:** 
- User is logged into Periplus account
- Items added to shopping cart
- Ability to simulate network interruption

**Test Steps:**
1. Navigate to cart page
2. Begin changing item quantity
3. Simulate network interruption
4. Complete the action
5. Restore network and refresh

**Expected Results:**
1. Cart page opens
2. Quantity field accepts input
3. Network interrupted
4. System handles interruption gracefully
5. Cart state is consistent after reconnection

### TC-CART-021: Interact with Cart During High Server Load
**Preconditions:** 
- User is logged into Periplus account
- Items added to shopping cart
- System under high load (simulated or during known high-traffic periods)

**Test Steps:**
1. Perform various cart operations during high load
2. Check consistency of cart contents

**Expected Results:**
1. System remains responsive or provides appropriate feedback
2. Cart operations complete successfully
3. Cart maintains data integrity

### TC-CART-022: Check Price Consistency in Cart vs. Product Page
**Preconditions:** 
- User is logged into Periplus account
- Shopping cart is empty
- Product catalog is accessible

**Test Steps:**
1. Note price on product detail page
2. Add item to cart
3. Navigate to cart page
4. Compare prices

**Expected Results:**
1. Product price is visible
2. Item added successfully
3. Cart page opens
4. Price in cart matches price on product page

### TC-CART-023: Add Product with Special Characters in Title/Description
**Preconditions:** 
- User is logged into Periplus account
- Locate product with special characters in title/description

**Test Steps:**
1. Add product with special characters to cart
2. View cart contents

**Expected Results:**
1. Product added successfully
2. Product title/details display correctly with special characters in cart