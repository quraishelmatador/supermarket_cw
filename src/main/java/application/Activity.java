package application;

/**
 *
 * @author Anas Hmaimou
 * @description Activity class for Supermarket System.
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Activity {

    private static int activityCounter = 0;
    private String activityID;
    private Product actProduct;
    private String actName;
    private int actQuantity;
    private String actDate;

    public Activity(Product actProduct, String actName, int actQuantity) {
        super();
        this.activityID = generateActivityID();
        this.actProduct = actProduct;
        this.actName = actName;
        this.actQuantity = actQuantity;
        this.actDate = getCurrentDateTime();
    }

    // Return a generated activity ID in the format: ACTXXXXXX (X being a digit)
    private String generateActivityID() {
        activityCounter++;
        return String.format("ACT%06d", activityCounter);
    }

    private String getCurrentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }

    public String getActName() {
        return actName;
    }

    public int getActQuantity() {
        return actQuantity;
    }

    public String getActDate() {
        return actDate;
    }

    public Product getActProduct() {
        return actProduct;
    }

}
