package application;

/**
 *
 * @author Shuayeb Ahmed & Kouresh Tayabaly
 * @description SceneController class to GUI panels in Supermarket System.
 */
import java.io.IOException;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class SceneController {

    private SupermarketSystem system = SupermarketSystem.getInstance();
    private Product selectedProduct = null;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // Security Check Fields
    @FXML
    private TextField userNameInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private Button passCheckButton;
    @FXML
    private Label passErrorLabel;

    // Table Views
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableView<Activity> activityTable;
    @FXML
    private TableView<CartItem> cartTable;
    @FXML
    private TableView<Product> receiptTable;

    // Add Product Panel Fields
    @FXML
    private TextField newProductName;
    @FXML
    private TextField newProductCost;
    @FXML
    private TextField newProductQuantity;
    @FXML
    private DatePicker newProductDate;
    @FXML
    private Label addProductStatus;

    // Adjust Stock Panel Fields
    @FXML
    private TextField stockProductID;
    @FXML
    private TextField stockProductName;
    @FXML
    private TextField stockReason;
    @FXML
    private TextField stockQuantity;
    @FXML
    private DatePicker stockDate;
    @FXML
    private Label stockStatus;
    @FXML
    private Label ErrorLabel;

    // Shopping Cart Panel Fields
    @FXML
    private TextField cartProductID;
    @FXML
    private TextField cartQuantity;
    @FXML
    private Label cartStatus;
    @FXML
    private Label cartTotal;

    // Receipt Panel
    @FXML
    private Label receiptTotal;

    private String lastPanel = "RootScene.fxml";

    // Delete Product Panel Fields
    @FXML
    private TextField deleteProductID;
    @FXML
    private Label deleteSearchStatus;
    @FXML
    private Label detailsLabel;
    @FXML
    private Label lblProductID;
    @FXML
    private Label displayProductID;
    @FXML
    private Label lblProductName;
    @FXML
    private Label displayProductName;
    @FXML
    private Label lblCost;
    @FXML
    private Label displayCost;
    @FXML
    private Label lblQuantity;
    @FXML
    private Label displayQuantity;
    @FXML
    private Label lblEntryDate;
    @FXML
    private Label displayEntryDate;
    @FXML
    private Label warningLabel;
    @FXML
    private Button confirmDeleteButton;
    @FXML
    private Label deleteStatus;

    private Product productToDelete = null;

    // ========== SCENE SWITCHING ==========
    private void switchPanel(ActionEvent e, String panelName) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/" + panelName));
        this.root = loader.load();
        this.stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        this.scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Object newController = loader.getController();
        if (newController instanceof SceneController) {
            if (panelName.equals("productDisplay.fxml")) {
                ((SceneController) newController).BuildProductTable();
            } else if (panelName.equals("shoppingCart.fxml")) {
                ((SceneController) newController).BuildProductTable();
                ((SceneController) newController).BuildCartTable();
                ((SceneController) newController).updateCartTotal();
            } else if (panelName.equals("productDetailsPanel.fxml")) {
                ((SceneController) newController).BuildActivityTable();
            } else if (panelName.equals("receiptPanel.fxml")) {
                ((SceneController) newController).BuildReceiptTable();
            }
        }
    }

    public void initialize() {
        if (passErrorLabel != null) {
            passErrorLabel.setVisible(false);
            passErrorLabel.setManaged(false);
        }
        if (addProductStatus != null) {
            addProductStatus.setVisible(false);
        }
        if (stockStatus != null) {
            stockStatus.setVisible(false);
        }
        if (ErrorLabel != null) {
            ErrorLabel.setVisible(false);
        }
    }

    public void switchToRootScene(ActionEvent e) throws IOException {
        switchPanel(e, "RootScene.fxml");
    }

    public void switchToAddProductPanel(ActionEvent e) throws IOException {
        switchPanel(e, "addProductPanel.fxml");
    }

    public void switchToAdjustStockPanel(ActionEvent e) throws IOException {
        switchPanel(e, "adjustStockPanel.fxml");
    }

    public void switchToMainPanel(ActionEvent e) throws IOException {
        switchPanel(e, "mainPanel.fxml");
    }

    public void switchToProductDetailsPanel(ActionEvent e) throws IOException {
        if (selectedProduct != null) {
            system.setCurrentSelectedProduct(selectedProduct);
            switchPanel(e, "productDetailsPanel.fxml");
        }
    }

    public void switchToProductDisplay(ActionEvent e) throws IOException {
        switchPanel(e, "productDisplay.fxml");
    }

    public void switchToReceiptPanel(ActionEvent e) throws IOException {
        switchPanel(e, "receiptPanel.fxml");
    }

    public void switchToSecurityCheck(ActionEvent e) throws IOException {
        switchPanel(e, "SecurityCheck.fxml");
    }

    public void switchToShoppingCart(ActionEvent e) throws IOException {
        switchPanel(e, "shoppingCart.fxml");
    }

    public void switchToStaffOptionsPanel(ActionEvent e) throws IOException {
        switchPanel(e, "staffOptionsPanel.fxml");
    }

    public void exitStage(ActionEvent e) {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    // ========== AUTHENTICATION ==========
    public void PassCheck(ActionEvent e) throws IOException {
        String username = userNameInput.getText();
        String password = passwordInput.getText();

        if (system.checkCredentials(username, password)) {
            switchPanel(e, "staffOptionsPanel.fxml");
        } else {
            passErrorLabel.setVisible(true);
            passErrorLabel.setManaged(true);
        }
    }

    // ========== BUILD TABLES ==========
    @SuppressWarnings({"unchecked", "deprecation"})
    public void BuildProductTable() {
        if (this.productTable != null && this.productTable.getColumns().isEmpty()) {
            TableColumn<Product, String> productIDColumn = new TableColumn<>("Product ID");
            productIDColumn.setCellValueFactory(new PropertyValueFactory<>("ProductID"));

            TableColumn<Product, String> productNameColumn = new TableColumn<>("Product Name");
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("ProductName"));

            TableColumn<Product, String> EntryDateColumn = new TableColumn<>("Entry Date");
            EntryDateColumn.setCellValueFactory(new PropertyValueFactory<>("EntryDate"));

            TableColumn<Product, Double> costColumn = new TableColumn<>("Cost");
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

            TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

            this.productTable.getColumns().addAll(productIDColumn, productNameColumn,
                    EntryDateColumn, costColumn, quantityColumn);

            productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            // Add selection listener
            productTable.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            selectedProduct = newSelection;
                        }
                    });
        }

        // Load data from system
        if (this.productTable != null) {
            productTable.getItems().clear();
            productTable.getItems().addAll(system.getProductList());
        }
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    public void BuildActivityTable() {
        if (this.activityTable != null && this.activityTable.getColumns().isEmpty()) {
            TableColumn<Activity, String> productIDColumn = new TableColumn<>("Product ID");
            productIDColumn.setCellValueFactory(cellData -> {
                Activity activity = cellData.getValue();
                String productID = activity.getActProduct().getProductID();
                return new ReadOnlyStringWrapper(productID);
            });

            TableColumn<Activity, String> productNameColumn = new TableColumn<>("Product Name");
            productNameColumn.setCellValueFactory(cellData -> {
                Activity activity = cellData.getValue();
                String productName = activity.getActProduct().getProductName();
                return new ReadOnlyStringWrapper(productName);
            });

            TableColumn<Activity, String> activityDateColumn = new TableColumn<>("Activity Date");
            activityDateColumn.setCellValueFactory(new PropertyValueFactory<>("actDate"));

            TableColumn<Activity, String> activityNameColumn = new TableColumn<>("Activity Name");
            activityNameColumn.setCellValueFactory(new PropertyValueFactory<>("actName"));

            TableColumn<Activity, Integer> quantityColumn = new TableColumn<>("Activity Quantity");
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("actQuantity"));

            this.activityTable.getColumns().addAll(productIDColumn, productNameColumn,
                    activityDateColumn, activityNameColumn, quantityColumn);

            activityTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }

        // Load activities for selected product
        Product product = system.getCurrentSelectedProduct();
        if (product != null && this.activityTable != null) {
            activityTable.getItems().clear();
            activityTable.getItems().addAll(system.getProductActivities(product));
        }
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    public void BuildCartTable() {
        if (this.cartTable != null && this.cartTable.getColumns().isEmpty()) {
            TableColumn<CartItem, String> productIDColumn = new TableColumn<>("Product ID");
            productIDColumn.setCellValueFactory(cellData -> {
                String id = cellData.getValue().getProduct().getProductID();
                return new javafx.beans.property.SimpleStringProperty(id);
            });

            TableColumn<CartItem, String> productNameColumn = new TableColumn<>("Product Name");
            productNameColumn.setCellValueFactory(cellData -> {
                String name = cellData.getValue().getProduct().getProductName();
                return new javafx.beans.property.SimpleStringProperty(name);
            });

            TableColumn<CartItem, Double> costColumn = new TableColumn<>("Unit Cost");
            costColumn.setCellValueFactory(cellData -> {
                double cost = cellData.getValue().getProduct().getCost();
                return new javafx.beans.property.SimpleDoubleProperty(cost).asObject();
            });

            TableColumn<CartItem, Integer> quantityColumn = new TableColumn<>("Cart Quantity");
            quantityColumn.setCellValueFactory(cellData -> {
                int qty = cellData.getValue().getQuantity(); // Cart quantity!
                return new javafx.beans.property.SimpleIntegerProperty(qty).asObject();
            });

            TableColumn<CartItem, Double> totalColumn = new TableColumn<>("Total Cost");
            totalColumn.setCellValueFactory(cellData -> {
                double total = cellData.getValue().getTotalCost();
                return new javafx.beans.property.SimpleDoubleProperty(total).asObject();
            });

            this.cartTable.getColumns().addAll(productIDColumn, productNameColumn,
                    costColumn, quantityColumn, totalColumn);

            cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

            // Add selection listener
            cartTable.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> {
                        if (newSelection != null) {
                            selectedProduct = newSelection.getProduct();
                        }
                    });
        }

        // Always refresh cart items
        if (this.cartTable != null) {
            cartTable.getItems().clear();
            cartTable.getItems().addAll(system.getShoppingCart());
        }
    }

    @SuppressWarnings({"unchecked", "deprecation"})
    public void BuildReceiptTable() {
        if (this.receiptTable != null && this.receiptTable.getColumns().isEmpty()) {
            TableColumn<Product, String> productIDColumn = new TableColumn<>("Product ID");
            productIDColumn.setCellValueFactory(new PropertyValueFactory<>("ProductID"));

            TableColumn<Product, String> productNameColumn = new TableColumn<>("Product Name");
            productNameColumn.setCellValueFactory(new PropertyValueFactory<>("ProductName"));

            TableColumn<Product, Double> costColumn = new TableColumn<>("Cost");
            costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));

            TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
            quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

            this.receiptTable.getColumns().addAll(productIDColumn, productNameColumn,
                    costColumn, quantityColumn);

            receiptTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }

        // Display receipt
        if (this.receiptTable != null && system.getLastReceipt() != null) {
            receiptTable.getItems().clear();
            double total = 0;
            for (CartItem item : system.getLastReceipt()) {
                receiptTable.getItems().add(item.getProduct());
                total += item.getTotalCost();
            }
            if (receiptTotal != null) {
                receiptTotal.setText(String.format("Total: £%.2f", total));
            }
        }
    }

    // ========== ADD PRODUCT ==========
    public void handleAddProduct(ActionEvent e) {
        try {
            // Validate inputs
            if (newProductName == null || newProductName.getText().trim().isEmpty()) {
                showStatus(addProductStatus, "Please enter a product name!", true);
                return;
            }

            String name = newProductName.getText().trim();
            double cost = Double.parseDouble(newProductCost.getText().trim());
            int quantity = Integer.parseInt(newProductQuantity.getText().trim());

            // Validate positive values
            if (cost < 0) {
                showStatus(addProductStatus, "Cost cannot be negative!", true);
                return;
            }
            if (quantity < 0) {
                showStatus(addProductStatus, "Quantity cannot be negative!", true);
                return;
            }

            String date;
            if (newProductDate != null && newProductDate.getValue() != null) {
                date = newProductDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } else {
                date = java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }

            if (system.addNewProduct(name, date, cost, quantity)) {
                showStatus(addProductStatus, "Product added successfully!", false);
                clearAddProductFields();
            } else {
                showStatus(addProductStatus, "Failed to add product!", true);
            }
        } catch (NumberFormatException ex) {
            showStatus(addProductStatus, "Invalid number format!", true);
        }
    }

    private void clearAddProductFields() {
        if (newProductName != null) {
            newProductName.clear();
        }
        if (newProductCost != null) {
            newProductCost.clear();
        }
        if (newProductQuantity != null) {
            newProductQuantity.clear();
        }
        if (newProductDate != null) {
            newProductDate.setValue(null);
        }
    }

    // ========== ADJUST STOCK ==========
    public void handleAddStock(ActionEvent e) {
        try {
            String productID = stockProductID.getText().trim();
            int quantity = Integer.parseInt(stockQuantity.getText().trim());

            // Validate positive quantity
            if (quantity < 0) {
                showStatus(stockStatus, "Quantity cannot be negative!", true);
                return;
            }

            if (system.addStock(productID, quantity)) {
                showStatus(stockStatus, "Stock added successfully!", false);
                clearStockFields();
            } else {
                showStatus(stockStatus, "Product not found or invalid quantity!", true);
            }
        } catch (NumberFormatException ex) {
            showStatus(stockStatus, "Invalid quantity format!", true);
        }
    }

    public void handleRemoveStock(ActionEvent e) {
        try {
            String productID = stockProductID.getText().trim();
            int quantity = Integer.parseInt(stockQuantity.getText().trim());
            String reason = stockReason != null ? stockReason.getText().trim() : "Stock Removed";

            // Validate positive quantity
            if (quantity < 0) {
                showStatus(stockStatus, "Quantity cannot be negative!", true);
                return;
            }

            if (reason.isEmpty()) {
                reason = "Stock Removed by Staff";
            }

            if (system.removeStock(productID, quantity, reason)) {
                showStatus(stockStatus, "Stock removed successfully!", false);
                clearStockFields();
            } else {
                showStatus(stockStatus, "Failed: Check product ID, quantity, or insufficient stock!", true);
            }
        } catch (NumberFormatException ex) {
            showStatus(stockStatus, "Invalid quantity format!", true);
        }
    }

    private void clearStockFields() {
        if (stockProductID != null) {
            stockProductID.clear();
        }
        if (stockQuantity != null) {
            stockQuantity.clear();
        }
        if (stockReason != null) {
            stockReason.clear();
        }
    }

// ========== SHOPPING CART ==========
    public void handleAddToCart(ActionEvent e) {
        try {
            String productID = cartProductID.getText().trim();
            int quantity = Integer.parseInt(cartQuantity.getText().trim());

            // Validate positive quantity
            if (quantity <= 0) {
                showStatus(cartStatus, "Quantity must be positive!", true);
                return;
            }

            if (system.addToCart(productID, quantity)) {
                showStatus(cartStatus, "Added to cart!", false);
                BuildCartTable();
                updateCartTotal();
                cartProductID.clear();
                cartQuantity.clear();
            } else {
                showStatus(cartStatus, "Failed: Already in cart or insufficient stock!", true);
            }
        } catch (NumberFormatException ex) {
            showStatus(cartStatus, "Invalid quantity format!", true);
        }
    }

    public void handleRemoveFromCart(ActionEvent e) {
        if (selectedProduct != null) {
            system.removeFromCart(selectedProduct.getProductID());
            BuildCartTable();
            updateCartTotal();
            showStatus(cartStatus, "Item removed from cart.", false);
            selectedProduct = null;
        } else {
            showStatus(cartStatus, "Please select an item to remove!", true);
        }
    }

    public void handlePurchase(ActionEvent e) throws IOException {
        if (system.getShoppingCart().isEmpty()) {
            showStatus(cartStatus, "Cart is empty!", true);
            return;
        }

        system.processPurchase();
        switchToReceiptPanel(e);
    }

    public void handleClearCart(ActionEvent e) {
        system.clearCart();
        BuildCartTable();
        updateCartTotal();
        showStatus(cartStatus, "Cart cleared.", false);
    }

    private void updateCartTotal() {
        if (cartTotal != null) {
            cartTotal.setText(String.format("Total: £%.2f", system.getCartTotal()));
        }
    }

// ========== SORTING ==========
    public void sortProductsByName(ActionEvent e) {
        system.sortProductsByName();
        BuildProductTable();
    }

    public void sortProductsById(ActionEvent e) {
        system.sortProductsById();
        BuildProductTable();
    }

    public void sortProductsByQuantity(ActionEvent e) {
        system.sortProductsByQuantity();
        BuildProductTable();
    }

// ========== HELPER METHODS ==========
    // Method to handle error messages: Green if all went well, and red if error.
    private void showStatus(Label label, String message, boolean isError) {
        if (label != null) {
            label.setText(message);
            label.setVisible(true);
            label.setManaged(true);
            if (isError) {
                label.setStyle("-fx-text-fill: red;"); // Sets label color to red
            } else {
                label.setStyle("-fx-text-fill: green;"); // Sets label color to green
            }
        }
    }

    // ========== DELETE PRODUCT FUNCTIONALITY ==========
    public void switchToDeleteProductPanel(ActionEvent e) throws IOException {
        switchPanel(e, "deleteProductPanel.fxml");
    }

    public void handleSearchProduct(ActionEvent e) {
        try {
            String productID = deleteProductID.getText().trim();

            if (productID.isEmpty()) {
                showDeleteSearchStatus("Please enter a Product ID!", true);
                hideProductDetails();
                return;
            }

            // Search for product using binary search
            Product product = system.binarySearchByID(productID);

            if (product != null) {
                productToDelete = product;
                showProductDetails(product);
                showDeleteSearchStatus("Product found! Review details below.", false);
            } else {
                productToDelete = null;
                hideProductDetails();
                showDeleteSearchStatus("Product not found with ID: " + productID, true);
            }
        } catch (Exception ex) {
            showDeleteSearchStatus("Error searching for product!", true);
            hideProductDetails();
        }
    }

    public void handleConfirmDelete(ActionEvent e) {
        if (productToDelete == null) {
            showDeleteStatus("No product selected to delete!", true);
            return;
        }

        String productID = productToDelete.getProductID();
        String productName = productToDelete.getProductName();

        // Delete the product using binary search 
        if (system.deleteProduct(productID)) {
            showDeleteStatus("✓ Product '" + productName + "' (ID: " + productID + ") deleted successfully!", false);
            productToDelete = null;
            hideProductDetails();
            if (deleteProductID != null) {
                deleteProductID.clear();
            }
        } else {
            showDeleteStatus("Failed to delete product!", true);
        }
    }

    private void showProductDetails(Product product) {
        if (detailsLabel != null) {
            detailsLabel.setVisible(true);
        }

        if (lblProductID != null) {
            lblProductID.setVisible(true);
        }
        if (displayProductID != null) {
            displayProductID.setText(product.getProductID());
            displayProductID.setVisible(true);
        }

        if (lblProductName != null) {
            lblProductName.setVisible(true);
        }
        if (displayProductName != null) {
            displayProductName.setText(product.getProductName());
            displayProductName.setVisible(true);
        }

        if (lblCost != null) {
            lblCost.setVisible(true);
        }
        if (displayCost != null) {
            displayCost.setText(String.format("£%.2f", product.getCost()));
            displayCost.setVisible(true);
        }

        if (lblQuantity != null) {
            lblQuantity.setVisible(true);
        }
        if (displayQuantity != null) {
            displayQuantity.setText(String.valueOf(product.getQuantity()));
            displayQuantity.setVisible(true);
        }

        if (lblEntryDate != null) {
            lblEntryDate.setVisible(true);
        }
        if (displayEntryDate != null) {
            displayEntryDate.setText(product.getEntryDate());
            displayEntryDate.setVisible(true);
        }

        if (warningLabel != null) {
            warningLabel.setVisible(true);
        }
        if (confirmDeleteButton != null) {
            confirmDeleteButton.setVisible(true);
        }
    }

    private void hideProductDetails() {
        if (detailsLabel != null) {
            detailsLabel.setVisible(false);
        }
        if (lblProductID != null) {
            lblProductID.setVisible(false);
        }
        if (displayProductID != null) {
            displayProductID.setVisible(false);
        }
        if (lblProductName != null) {
            lblProductName.setVisible(false);
        }
        if (displayProductName != null) {
            displayProductName.setVisible(false);
        }
        if (lblCost != null) {
            lblCost.setVisible(false);
        }
        if (displayCost != null) {
            displayCost.setVisible(false);
        }
        if (lblQuantity != null) {
            lblQuantity.setVisible(false);
        }
        if (displayQuantity != null) {
            displayQuantity.setVisible(false);
        }
        if (lblEntryDate != null) {
            lblEntryDate.setVisible(false);
        }
        if (displayEntryDate != null) {
            displayEntryDate.setVisible(false);
        }
        if (warningLabel != null) {
            warningLabel.setVisible(false);
        }
        if (confirmDeleteButton != null) {
            confirmDeleteButton.setVisible(false);
        }
        if (deleteStatus != null) {
            deleteStatus.setVisible(false);
        }
    }

    private void showDeleteSearchStatus(String message, boolean isError) {
        if (deleteSearchStatus != null) {
            deleteSearchStatus.setText(message);
            deleteSearchStatus.setVisible(true);
            if (isError) {
                deleteSearchStatus.setStyle("-fx-text-fill: red;");
            } else {
                deleteSearchStatus.setStyle("-fx-text-fill: green;");
            }
        }
    }

    private void showDeleteStatus(String message, boolean isError) {
        if (deleteStatus != null) {
            deleteStatus.setText(message);
            deleteStatus.setVisible(true);
            if (isError) {
                deleteStatus.setStyle("-fx-text-fill: red;");
            } else {
                deleteStatus.setStyle("-fx-text-fill: green;");
            }
        }
    }
}
