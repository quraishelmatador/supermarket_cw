package application;

/**
 *
 * @author Anas Hmaimou
 * @description CartItem class for Supermarket System to handle items in cart
 */
public class CartItem {

    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalCost() {
        return product.getCost() * quantity;
    }
}
