package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller class for the Modify Product Screen
 *
 * @author Scott Alesci
 */
public class ModifyProduct implements Initializable {
    public TextField productIDField;
    public TextField productNameField;
    public TextField productInvField;
    public TextField productPriceField;
    public TextField productMinField;
    public TextField productMaxField;
    public TextField queryField;
    public TableView partTable;
    public TableView aPartTable;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partStockCol;
    public TableColumn partPriceCol;
    public TableColumn aPartIDCol;
    public TableColumn aPartNameCol;
    public TableColumn aPartPriceCol;
    public TableColumn aPartStockCol;
    public Label errorLabel;
    private static Product productReceived = null;

    /**
     * Static method used to send the product to the ModifyProduct class
     *
     * @param product the product to send to the ModifyProduct class
     */
    public static void sendProduct(Product product){
        productReceived = product;
    }

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Initializes the controller and sets the fields to the product's attributes
     *
     * @param url the location that is used to resolve paths for the root, this is null if the location is not known
     * @param resourceBundle the resources used to localize the root obj, this is null if the root obj was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        associatedParts.clear();

        // set the fields to the product's attributes and associated parts
        productIDField.setText(String.valueOf(productReceived.getId()));
        productNameField.setText(productReceived.getName());
        productInvField.setText(String.valueOf(productReceived.getStock()));
        productPriceField.setText(String.valueOf(productReceived.getPrice()));
        productMinField.setText(String.valueOf(productReceived.getMin()));
        productMaxField.setText(String.valueOf(productReceived.getMax()));
        for (Part part : productReceived.getAllAssociatedParts()){
            associatedParts.add(part);
        }

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        aPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        aPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        aPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        aPartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        allParts = Inventory.getAllParts();
        partTable.setItems(allParts);

        // Show only the associated parts for the specific product
        aPartTable.setItems(associatedParts);
    }

    /**
     * Takes the selected part from the part table and adds it to the associated part table
     *
     * @param actionEvent when the add associated part button is clicked
     */
    public void onAddAssociatedPart(ActionEvent actionEvent) {
        // grab the selected part
        // add it to the associatedParts list
        Part selectedPart = (Part) partTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
        }else{
            associatedParts.add(selectedPart);
            aPartTable.setItems(associatedParts);
        }

    }

    /**
     * After confirming action, removes the part from the associated part table
     *
     * @param actionEvent when the remove part button is clicked
     */
    public void onRemoveAssociatedPart(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            associatedParts.remove((Part) aPartTable.getSelectionModel().getSelectedItem());
            aPartTable.setItems(associatedParts);
        }
    }

    /**
     * Does error checks, sets the products attributes, empties the products associated parts, adds the current associated parts, and goes to the FirstScreen
     *
     * @param actionEvent when the Save button is clicked
     * @throws IOException from FXMLLoader
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        int stock = 0, min = 0, max = 0;
        double price = 0;
        String name = productNameField.getText();
        errorLabel.setText("");

        try{
            stock = Integer.parseInt(productInvField.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Stock needs to be a number");
        }
        try{
            price = Double.parseDouble(productPriceField.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Price needs to be number format");
        }
        try{
            min = Integer.parseInt(productMinField.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Min needs to be a number");
        }
        try{
            max = Integer.parseInt(productMaxField.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Max needs to be a number");
        }
        // Check to make sure Min is less than Max
        if (max < stock || stock < min){
            errorLabel.setText("Min must be less than Max and Inventory \n" +
                    "must be in between them");
        }
        // Make sure there are no errors then set the product
        if (Objects.equals(errorLabel.getText(), "")){
            productReceived.setName(name);
            productReceived.setPrice(price);
            productReceived.setStock(stock);
            productReceived.setMin(min);
            productReceived.setMax(max);

            while (!productReceived.getAllAssociatedParts().isEmpty()){
                productReceived.getAllAssociatedParts().clear();
            }

            for (Part part : associatedParts) {
                productReceived.addAssociatedPart(part);
            }

            // Go to the main screen
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/FirstScreen.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Navigates to the FirstScreen (after confirming)
     *
     * @param actionEvent when the Cancel button is clicked
     * @throws IOException from FXMLLoader
     */
    public void onCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to discard your changes?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/FirstScreen.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }

    }

    /**
     * When you type a character in the search part field, this calls the queryPart method below and sets the results to the partTable
     */
    public void onQuery() {
        String query = queryField.getText();

        ObservableList<Part> parts = queryPart(query);

        partTable.setItems(parts);
    }

    /**
     * Returns the list of parts that are included in the search, shows an error if there are no matches
     *
     * @param partialName the String or ID that is searched
     * @return the list of parts that match the search
     */
    private ObservableList<Part> queryPart(String partialName){
        ObservableList<Part> namedParts = FXCollections.observableArrayList();

        ObservableList<Part> allParts = Inventory.getAllParts();
        // int id = Integer.parseInt(partialName);

        for (Part tp : allParts){
            int tpid = 0;
            try{
                tpid = Integer.parseInt(partialName);
            }catch (NumberFormatException e){}
            if(tp.getName().contains(partialName) || tp.getId() == tpid){
                namedParts.add(tp);
            }
        }
        if (namedParts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Search Results");
            alert.setContentText("Your search is empty");
            alert.showAndWait();
        }
        return namedParts;
    }
}
