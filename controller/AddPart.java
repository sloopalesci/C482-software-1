package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is the controller class for the add part screen
 *
 * @author Scott Alesci
 */
public class AddPart implements Initializable {
    public TextField addPartIDField;
    public TextField addPartNameField;
    public TextField addPartPriceField;
    public TextField addPartMinField;
    public TextField addPartMaxField;
    public TextField addPartInvField;
    public TextField addPartMachineIDField;
    public RadioButton inHouseToggle;
    public RadioButton OutsourcedToggle;
    public ToggleGroup partToggle;
    public Label inHouseLabel;
    public Label errorLabel;

    /**
     * Initializes the AddPart controller
     *
     * @param url the location that is used to resolve paths for the root, this is null if the location is not known
     * @param resourceBundle the resources used to localize the root obj, this is null if the root obj was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * confirms you want to go to FirstScreen
     *
     * @param actionEvent When you click on the Cancel button
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
     * RUNTIME ERROR: in this method we are taking a text field (String) and setting it to different parameters for a part
     * (int and double). To convert the String to the right data type I used the parsing methods Integer.parseInt() and
     * Double.parseDouble(). However, the user can still enter the wrong data types.
     * To account for this, I used a try/catch statement to catch the error and display an error message when the wrong
     * type is given (a String was given when expecting an int).
     *
     * This method error checks, creates, adds the part to the Inventory, and goes to FirstScreen
     *
     * @param actionEvent when you click on the Save button
     * @throws IOException from FXMLLoader
     */
    public void onSave(ActionEvent actionEvent) throws IOException{

        // Create the local variables to save to the new part
        double price = 0;
        int id = Inventory.getAllParts().size() + 1, stock = 0, min = 0, max = 0, machineID = 0;
        errorLabel.setText("");

        String name = addPartNameField.getText();
        String companyName = addPartMachineIDField.getText();

        // try catch to make sure the right types are passed in
        try{
            price = Double.parseDouble(addPartPriceField.getText());
        }catch (NumberFormatException e){
            errorLabel.setText("Price needs to be number format");
        }
        try{
            stock = Integer.parseInt(addPartInvField.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Stock needs to be a number");
        }
        try{
            min = Integer.parseInt(addPartMinField.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Min needs to be a number");
        }
        try{
            max = Integer.parseInt(addPartMaxField.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Max needs to be a number");
        }
        if (inHouseToggle.isSelected()){
            try{
                machineID = Integer.parseInt(addPartMachineIDField.getText());
            }catch(NumberFormatException e){
                errorLabel.setText("Machine ID needs to be a number");
            }
        }
        // Check to make sure Min is less than Max
        if (max < stock || stock < min){
            errorLabel.setText("Min must be less than Max and Inventory\n" +
                    "must be in between them");
        }

        // Check to make sure there are no errors
        if (Objects.equals(errorLabel.getText(), "")){
            // Check to see if this is in house or outsourced
            if (inHouseToggle.isSelected()) {
                // Create an In House part and pass in all the fields
                InHouse newPart = new InHouse(id, name, price, stock, min, max, machineID);
                Inventory.addPart(newPart);
            }else if (OutsourcedToggle.isSelected()) {
                // Create an Outsourced part and pass in all the fields
                Outsourced newPart = new Outsourced(id, name, price, stock, min, max, companyName);
                Inventory.addPart(newPart);
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
     * Sets the superclass label to In House
     *
     * @param actionEvent when you click the "In House" toggle
     */
    public void onInHouseToggle(ActionEvent actionEvent) {
        inHouseLabel.setText("Machine ID");
    }

    /**
     * Sets the superclass label to Outsourced
     *
     * @param actionEvent when you click the "Outsourced" toggle
     */
    public void onOutsourcedToggle(ActionEvent actionEvent) {
        inHouseLabel.setText("Company");
    }
}
