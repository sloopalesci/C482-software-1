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
 * The controller class for the AddProduct screen
 *
 * @author Scott Alesci
 */
public class AddProduct implements Initializable {
    public TextField productIDField;
    public TextField productNameField;
    public TextField productInvField;
    public TextField productPriceField;
    public TextField productMinField;
    public TextField productMaxField;
    public TableView partTable;
    public TableView aPartTable;
    public TextField queryField;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partInvCol;
    public TableColumn partPriceCol;
    public TableColumn aPartIDCol;
    public TableColumn aPartNameCol;
    public TableColumn aPartInvCol;
    public TableColumn aPartPriceCol;
    public Label errorLabel;

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     *
     * @param url the location that is used to resolve paths for the root, this is null if the location is not known
     * @param resourceBundle the resources used to localize the root obj, this is null if the root obj was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        associatedParts.clear();
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        allParts = Inventory.getAllParts();
        partTable.setItems(allParts);

        // Sets the associated parts list
        aPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        aPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        aPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        aPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }

    /**
     * This is the button to add the part to the associated part list
     */
    public void onAddAssociatedPart() {
        associatedParts.add((Part) partTable.getSelectionModel().getSelectedItem());
        aPartTable.setItems(associatedParts);
    }

    /**
     * This is the button to remove the associated part from the associated part list (after confirming)
     */
    public void onRemoveAssociatedPart() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            associatedParts.remove((Part) aPartTable.getSelectionModel().getSelectedItem());
            aPartTable.setItems(associatedParts);
        }
    }

    /**
     * Error checks, creates, adds the product to the Inventory, and goes to First Screen
     *
     * @param actionEvent when you click the save button
     * @throws IOException from FXMLLoader
     */
    public void onSave(ActionEvent actionEvent) throws IOException {
        // declare the variables to create the product object
        int id = Inventory.getAllProducts().size() + 1, stock = 0, min = 0, max = 0;
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
            Product newProduct = new Product(id, name,price, stock, min, max);
            for (Part part : associatedParts) newProduct.addAssociatedPart(part);

            Inventory.addProduct(newProduct);

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
     * Confirms you'd like to cancel and goes to FirstScreen
     *
     * @param actionEvent When you click the cancel button
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
     * When you type a character in the search part field, this calls the queryPart method below
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
