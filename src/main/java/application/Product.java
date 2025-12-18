package application;

/**
 *
 * @author Shuayeb Ahmed & Kouresh Tayabaly
 * @description Product class for Supermarket System
 */
public class Product {

    private static int productCounter = 0; // For generating unique IDs
    private String productID;
    private String productName;
    private String entryDate;
    private double cost;
    private int quantity;
    private Activity[] actList = new Activity[4]; // Specifies tha array size (4)
    private int noOfActs = 0; // To keep count of activities

    public Product(String productName, String entryDate, double cost, int quantity) {
        this.productID = generateProductID();//  Generates unique ID
        this.productName = productName;
        this.entryDate = entryDate;
        this.cost = cost;
        this.quantity = quantity;
    }

    // Method to generate unique product ID
    private String generateProductID() {
        productCounter++;
        return String.format("P%05d", productCounter); // Format String as PXXXXX (X being a digit)
    }

    // Method to add activity to product (keeps last 4)
    public void addActivity(Activity activity) {
        // If less than 4 activities, put the latest one at next index
        if (noOfActs < 4) {
            actList[noOfActs] = activity;
            noOfActs++;
        } else {
            // Else, shift all activities to the left and put latest one at the last index
            for (int i = 0; i < 3; i++) {
                actList[i] = actList[i + 1];
            }
            actList[3] = activity;
        }
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Activity[] getActList() {
        return actList;
    }

    public void setActList(Activity[] actList) {
        this.actList = actList;
    }

    public int getNoOfActs() {
        return noOfActs;
    }

    public void setNoOfActs(int noOfActs) {
        this.noOfActs = noOfActs;
    }
}
