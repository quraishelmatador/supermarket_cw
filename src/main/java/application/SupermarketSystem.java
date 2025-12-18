package application;

/**
 *
 * @author Anas Hmaimou & Kouresh Tayabaly
 * @description SupermarketSystem to manage supermarket
 */
import java.util.ArrayList;
import java.util.List;

public class SupermarketSystem {

    private static SupermarketSystem instance;
    private List<Product> productList; // ArrayList
    private CustomLinkedList<CartItem> shoppingCart; // Custom Linked List implementation
    private static final String STAFF_PASSWORD = "admin123";
    private static final String STAFF_USERNAME = "staff";

    // For passing selected product between scenes
    private Product currentSelectedProduct;
    private List<CartItem> lastReceipt;

    private SupermarketSystem() {
        this.productList = new ArrayList<>();
        this.shoppingCart = new CustomLinkedList<>(); // Using custom linked list
        addSampleProducts();
    }

    public static SupermarketSystem getInstance() {
        if (instance == null) {
            instance = new SupermarketSystem();
        }
        return instance;
    }

    private void addSampleProducts() {
        Product p1 = new Product("Spicy Sticks", "10/05/2024", 5.0, 1000);
        Activity a1 = new Activity(p1, "Initial Stock", 1000);
        p1.addActivity(a1);
        productList.add(p1);

        Product p2 = new Product("Milk", "10/05/2024", 1.50, 200);
        Activity a2 = new Activity(p2, "Initial Stock", 200);
        p2.addActivity(a2);
        productList.add(p2);

        Product p3 = new Product("Bread", "10/05/2024", 0.99, 150);
        Activity a3 = new Activity(p3, "Initial Stock", 150);
        p3.addActivity(a3);
        productList.add(p3);
    }

    // ========== PRODUCT MANAGEMENT ==========
    public List<Product> getProductList() {
        return productList;
    }

    public boolean addNewProduct(String name, String date, double cost, int quantity) {
        Product newProduct = new Product(name, date, cost, quantity);
        Activity activity = new Activity(newProduct, "Initial Stock", quantity);
        newProduct.addActivity(activity);
        return productList.add(newProduct);
    }

    // BINARY SEARCH ALGORITHM  for Deleting product --> Efficient for large product databases 0(log n)
    // Products must be sorted for binary search to work
    public Product binarySearchByID(String productID) {
        // Create a sorted copy to avoid modifying the original list
        List<Product> sortedList = new ArrayList<>(productList);

        // Sort the copy by product ID
        for (int i = 0; i < sortedList.size() - 1; i++) {
            for (int j = 0; j < sortedList.size() - i - 1; j++) {
                Product current = sortedList.get(j);
                Product next = sortedList.get(j + 1);

                if (current.getProductID().compareTo(next.getProductID()) > 0) {
                    sortedList.set(j, next);
                    sortedList.set(j + 1, current);
                }
            }
        }

        // Now perform binary search on the sorted copy
        int left = 0;
        int right = sortedList.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Product midProduct = sortedList.get(mid);
            int comparison = midProduct.getProductID().compareTo(productID);

            if (comparison == 0) {
                return midProduct; // Found the product
            } else if (comparison < 0) {
                left = mid + 1; // Search right half
            } else {
                right = mid - 1; // Search left half
            }
        }
        return null; // Product not found
    }

    // Delete product using binary search
    public boolean deleteProduct(String productID) {
        Product product = binarySearchByID(productID);
        if (product != null) {
            return productList.remove(product);
        }
        return false;
    }

    // Lineear Search
    public Product findProductById(String productID) {
        for (Product product : productList) {
            if (product.getProductID().equals(productID)) {
                return product;
            }
        }
        return null;
    }

    public Product findProductByName(String productName) {
        for (Product product : productList) {
            if (product.getProductName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null;
    }

    // ========== SORTING METHODS ==========
    // Bubble sort by product name
    public void sortProductsByName() {
        for (int i = 0; i < productList.size() - 1; i++) {
            for (int j = 0; j < productList.size() - i - 1; j++) {
                Product current = productList.get(j);
                Product next = productList.get(j + 1);

                if (current.getProductName().compareToIgnoreCase(next.getProductName()) > 0) {
                    productList.set(j, next);
                    productList.set(j + 1, current);
                }
            }
        }
    }

    // Bubble sort by product ID
    public void sortProductsById() {
        for (int i = 0; i < productList.size() - 1; i++) {
            for (int j = 0; j < productList.size() - i - 1; j++) {
                Product current = productList.get(j);
                Product next = productList.get(j + 1);

                if (current.getProductID().compareTo(next.getProductID()) > 0) {
                    productList.set(j, next);
                    productList.set(j + 1, current);
                }
            }
        }
    }

    // Bubble sort by quantity
    public void sortProductsByQuantity() {
        for (int i = 0; i < productList.size() - 1; i++) {
            for (int j = 0; j < productList.size() - i - 1; j++) {
                Product current = productList.get(j);
                Product next = productList.get(j + 1);

                if (current.getQuantity() < next.getQuantity()) {
                    productList.set(j, next);
                    productList.set(j + 1, current);
                }
            }
        }
    }

    // ========== STOCK MANAGEMENT ==========
    public boolean addStock(String productID, int quantity) {
        Product product = findProductById(productID);
        if (product == null || quantity <= 0) {
            return false;
        }

        // Update quantity
        product.setQuantity(product.getQuantity() + quantity);

        // Create activity
        Activity activity = new Activity(product, "Stock Added", quantity);
        product.addActivity(activity);

        return true;
    }

    public boolean removeStock(String productID, int quantity, String reason) {
        Product product = findProductById(productID);
        if (product == null || quantity <= 0 || product.getQuantity() < quantity) {
            return false;
        }

        // Update quantity
        product.setQuantity(product.getQuantity() - quantity);

        // Create activity (negative quantity to indicate removal)
        Activity activity = new Activity(product, reason, -quantity);
        product.addActivity(activity);

        return true;
    }

    // ========== SHOPPING CART (Using Custom LinkedList) ==========
    public List<CartItem> getShoppingCart() {
        // Convert custom linked list to ArrayList for display
        List<CartItem> cartList = new ArrayList<>();
        for (int i = 0; i < shoppingCart.size(); i++) {
            cartList.add(shoppingCart.get(i));
        }
        return cartList;
    }

    public boolean addToCart(String productID, int quantity) {
        Product product = findProductById(productID);
        if (product == null) {
            return false;
        }

        // Check if already in cart
        for (int i = 0; i < shoppingCart.size(); i++) {
            if (shoppingCart.get(i).getProduct().getProductID().equals(productID)) {
                return false; // Already in cart
            }
        }

        // Check stock availability
        if (product.getQuantity() < quantity) {
            return false;
        }

        shoppingCart.add(new CartItem(product, quantity));
        return true;
    }

    public boolean updateCartQuantity(String productID, int newQuantity) {
        for (int i = 0; i < shoppingCart.size(); i++) {
            CartItem item = shoppingCart.get(i);
            if (item.getProduct().getProductID().equals(productID)) {
                if (item.getProduct().getQuantity() < newQuantity) {
                    return false;
                }
                item.setQuantity(newQuantity);
                return true;
            }
        }
        return false;
    }

    public boolean removeFromCart(String productID) {
        for (int i = 0; i < shoppingCart.size(); i++) {
            if (shoppingCart.get(i).getProduct().getProductID().equals(productID)) {
                shoppingCart.remove(i);
                return true;
            }
        }
        return false;
    }

    public void clearCart() {
        shoppingCart.clear();
    }

    public double getCartTotal() {
        double total = 0;
        for (int i = 0; i < shoppingCart.size(); i++) {
            total += shoppingCart.get(i).getTotalCost();
        }
        return total;
    }

    // ========== PURCHASE PROCESSING ==========
    public List<CartItem> processPurchase() {
        List<CartItem> receipt = new ArrayList<>();

        for (int i = 0; i < shoppingCart.size(); i++) {
            CartItem item = shoppingCart.get(i);
            Product product = item.getProduct();
            int purchaseQty = item.getQuantity();

            // Adjust stock
            product.setQuantity(product.getQuantity() - purchaseQty);

            // Create activity (negative to show removal)
            Activity activity = new Activity(product, "Purchase", -purchaseQty);
            product.addActivity(activity);

            // Add to receipt
            receipt.add(new CartItem(product, purchaseQty));
        }

        lastReceipt = new ArrayList<>(receipt);
        shoppingCart.clear();

        return receipt;
    }

    // ========== ACTIVITY MANAGEMENT  ==========
    public List<Activity> getProductActivities(Product product) {
        List<Activity> activities = new ArrayList<>();
        Activity[] actList = product.getActList();

        for (int i = 0; i < product.getNoOfActs(); i++) {
            if (actList[i] != null) {
                activities.add(actList[i]);
            }
        }

        // Seelction  
        sortActivitiesByQuantity(activities);
        return activities;
    }

    // Selection sort: Sort activities by quantity
    private void sortActivitiesByQuantity(List<Activity> activities) {
        for (int i = 0; i < activities.size() - 1; i++) {
            int maxIndex = i;

            // Find the maximum quantity in remaining unsorted array
            for (int j = i + 1; j < activities.size(); j++) {
                // Use absolute value to compare quantities
                if (Math.abs(activities.get(j).getActQuantity())
                        > Math.abs(activities.get(maxIndex).getActQuantity())) {
                    maxIndex = j;
                }
            }

            // Swap the found maximum element with the first element
            if (maxIndex != i) {
                Activity temp = activities.get(i);
                activities.set(i, activities.get(maxIndex));
                activities.set(maxIndex, temp);
            }
        }
    }

    // ========== AUTHENTICATION ==========
    public boolean checkCredentials(String username, String password) {
        return STAFF_USERNAME.equals(username) && STAFF_PASSWORD.equals(password);
    }

    // ========== SELECTED PRODUCT MANAGEMENT ==========
    public void setCurrentSelectedProduct(Product product) {
        this.currentSelectedProduct = product;
    }

    public Product getCurrentSelectedProduct() {
        return currentSelectedProduct;
    }

    // ========== RECEIPT MANAGEMENT ==========
    public List<CartItem> getLastReceipt() {
        return lastReceipt;
    }
}
