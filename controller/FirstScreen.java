package controller;

import javafx.application.Platform;
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
import model.Part;
import model.Inventory;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.ModifyPart.sendPart;
import static controller.ModifyProduct.sendProduct;
/**
 * The FirstScreen controller
 *
 * @author Scott Alesci
 * */
public class FirstScreen implements Initializable {
    public Label theLabel;
    public TableColumn partIDCol;
    public TableColumn partNameCol;
    public TableColumn partPriceCol;
    public TableColumn partStockCol;
    public TableColumn partMinCol;
    public TableColumn partMaxCol;
    public TableView partTable;
    public TableView productTable;
    public TableColumn productIDCol;
    public TableColumn productNameCol;
    public TableColumn productPriceCol;
    public TableColumn productStockCol;
    public TableColumn productMinCol;
    public TableColumn productMaxCol;
    public TextField partQuery;
    public TextField productSearchField;

    private ObservableList<Part> allParts = FXCollections.observableArrayList();
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param url the location that is used to resolve paths for the root, this is null if the location is not known
     * @param resourceBundle resourceBundle the resources used to localize the root obj, this is null if the root obj was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // This is pointing the partTable to the observableList, allParts
        // Here we have to link up all the columns to the object parameters
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partMinCol.setCellValueFactory(new PropertyValueFactory<>("min"));
        partMaxCol.setCellValueFactory(new PropertyValueFactory<>("max"));

        allParts = Inventory.getAllParts();
        partTable.setItems(allParts);

        productIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productMinCol.setCellValueFactory(new PropertyValueFactory<>("min"));
        productMaxCol.setCellValueFactory(new PropertyValueFactory<>("max"));

        allProducts = Inventory.getAllProducts();
        productTable.setItems(allProducts);



    }

    /**
     * Navigates to the AddPart screen
     *
     * @param actionEvent when the add part button is clicked
     * @throws IOException from FXMLLoader
     */
    public void onAddPart( ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Removes the selected part from the allParts table
     *
     * @param actionEvent when the delete button is pressed
     * @throws IOException from FXMLLoader
     */
    public void onDeletePart(ActionEvent actionEvent) throws IOException{
        Part tempPart = (Part)partTable.getSelectionModel().getSelectedItem();

        if (tempPart == null) {
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                allParts.remove(tempPart);
            }
        }
    }

    /**
     * Takes the selected part, sends it to the ModifyPart class, and switches to the ModifyPart screen
     *
      * @param actionEvent when the modify part button is clicked
     * @throws IOException from FXMLLoader
     */
    public void onModifyPart(ActionEvent actionEvent) throws IOException{
        Part tempPart = (Part)partTable.getSelectionModel().getSelectedItem();
        if (tempPart == null){
        }else{
            sendPart(tempPart);

            // Go to the ModifyPart screen
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Navigates to the AddProduct screen
     *
      * @param actionEvent when the add product button is clicked
     * @throws IOException from FXMLLoader
     */
    public void onAddProduct(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Takes the selected product, sends it to the ModifyProduct class, and switches to the ModifyProduct screen
     *
      * @param actionEvent when the modify product button is clicked
     * @throws IOException from FXMLLoader
     */
    public void onModifyProduct(ActionEvent actionEvent) throws IOException{
        Product tempProduct = (Product)productTable.getSelectionModel().getSelectedItem();
        if (tempProduct == null){
        }else{
            sendProduct(tempProduct);

            // Go to the ModifyProduct screen
            Parent root = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
        }

    }

    /**
     * Takes the selected product, checks if there are associated parts, if not, deletes the product
     *
     * @param actionEvent when delete button is clicked
     * @throws IOException from FXMLLoader
     */
    public void onDeleteProduct(ActionEvent actionEvent) throws IOException {
        Product tempProduct = (Product) productTable.getSelectionModel().getSelectedItem();
        if (tempProduct == null){
        }else{
            if (!tempProduct.getAllAssociatedParts().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING, "You must remove associated parts before deleting product");
                Optional<ButtonType> result = alert.showAndWait();
            }else
            {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this product?");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK)
                {
                    allProducts.remove(tempProduct);
                }
            }
        }
    }

    /**
     * When you type a character in the search part field, this calls the queryPart method below and sets the results to the partTable
     */
    public void onPartSearch() {
        String query = partQuery.getText();

        ObservableList<Part> parts = queryPart(query);

        partTable.setItems(parts);
    }

    /**
     * Returns the list of parts that are included in the search
     *
      * @param partialName the string you're searching for
     * @return the parts that resulted from the search
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

    /**
     * The app terminates
     * 
     * @param actionEvent when the Exit button is clicked
     */
    public void onExit(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * When you type a character in the search product field, this calls the queryProduct method below and sets the results to the productTable
     */
    public void onProductSearch() {
        String query = productSearchField.getText();

        ObservableList<Product> product = queryProduct(query);

        productTable.setItems(product);
    }

    /**
     * Returns the list of products that are included in the search, shows an error if there are no matches
     *
     * @param partialName the String or ID that is searched
     * @return the list of products that match the search
     */
    private ObservableList<Product> queryProduct(String partialName){
        ObservableList<Product> namedProducts = FXCollections.observableArrayList();

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        for (Product tp : allProducts){
            int tpid = 0;
            try{
                tpid = Integer.parseInt(partialName);
            }catch (NumberFormatException e){}
            if(tp.getName().contains(partialName) || tp.getId() == tpid){
                namedProducts.add(tp);
            }
        }
        if (namedProducts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Search Results");
            alert.setContentText("Your search is empty");
            alert.showAndWait();
        }
        return namedProducts;
    }

}
