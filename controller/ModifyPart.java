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
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The Modify Part controller
 *
 * @author Scott Alesci
 */
public class ModifyPart implements Initializable {
    public RadioButton inHouseToggle;
    public RadioButton OutsourcedToggle;
    public ToggleGroup partToggle;
    public TextField modifyPartNameField;
    public TextField modifyPartPriceField;
    public TextField modifyPartMinField;
    public TextField modifyPartMaxField;
    public TextField modifyPartInvField;
    public TextField modifyPartMachineIDField;
    public TextField modifyPartIDField;
    public Label inHouseLabel;
    public Label errorLabel;

    private static Part partReceived = null;

    /**
     * Static method used to send the part to the ModifyPart class
     *
     * @param part the part that is going to be modified
     */
    public static void sendPart(Part part){
        partReceived = part;
    }

    /**
     * Initializes the controller and sets the fields to the part's attributes.
     *
     * @param url the location that is used to resolve paths for the root, this is null if the location is not known
     * @param resourceBundle resourceBundle the resources used to localize the root obj, this is null if the root obj was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        // set the fields to the part's attributes
        modifyPartIDField.setText(String.valueOf(partReceived.getId()));
        modifyPartNameField.setText(partReceived.getName());
        modifyPartPriceField.setText(String.valueOf(partReceived.getPrice()));
        modifyPartMinField.setText(String.valueOf(partReceived.getMin()));
        modifyPartInvField.setText(String.valueOf(partReceived.getStock()));
        modifyPartMaxField.setText(String.valueOf(partReceived.getMax()));

        // choose the toggle, set the label, and get the ID or Name
        if (partReceived instanceof InHouse){
            inHouseToggle.setSelected(true);
            inHouseLabel.setText("Machine ID");
            modifyPartMachineIDField.setText(String.valueOf(((InHouse) partReceived).getMachineID()));
        }else if(partReceived instanceof Outsourced){
            OutsourcedToggle.setSelected(true);
            inHouseLabel.setText("Company");
            modifyPartMachineIDField.setText(((Outsourced) partReceived).getCompanyName());
        }

    }

    /**
     * Changes the text of the inHouseLabel
     *
     * @param actionEvent When the InHouse toggle is selected
     */
    public void onInHouseToggle(ActionEvent actionEvent) {
        inHouseLabel.setText("Machine ID");
    }

    /**
     * Changes the text of the inHouseLabel
     *
     * @param actionEvent When the Outsourced toggle is selected
     */
    public void onOutsourcedToggle(ActionEvent actionEvent) {
        inHouseLabel.setText("Company");
    }

    /**
     * Checks for errors, creates the appropriate type of part, adds it to Inventory, and goes to the FirstScreen
     *
     * @param actionEvent when the Save button is pressed
     * @throws IOException from FXMLLoader
     */
    public void onSave(ActionEvent actionEvent) throws IOException{

        double price = 0;
        int stock = 0, min = 0, max = 0, machineID = 0;
        errorLabel.setText("");
        int index = partReceived.getId();
        String name = modifyPartNameField.getText();

        try{
            price = Double.parseDouble(modifyPartPriceField.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Price needs to be number format");
        }
        try{
            stock = Integer.parseInt(modifyPartInvField.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Stock needs to be a number");
        }
        try{
            min = Integer.parseInt(modifyPartMinField.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Min needs to be a number");
        }
        try{
            max = Integer.parseInt(modifyPartMaxField.getText());
        }catch(NumberFormatException e){
            errorLabel.setText("Max needs to be a number");
        }
        if (inHouseToggle.isSelected()) {
            try {
                machineID = Integer.parseInt(modifyPartMachineIDField.getText());
            } catch (NumberFormatException e) {
                errorLabel.setText("Machine ID needs to be a number");
            }
        }
        // Check to make sure Min is less than Max
        if (max < stock || stock < min){
            errorLabel.setText("Min must be less than Max and Inventory \n" +
                    "must be in between them");
        }
        if (Objects.equals(errorLabel.getText(), "")){
            if (inHouseToggle.isSelected()) {
                // Create an In House part and pass in all the fields
                InHouse newPart = new InHouse(index, name, price, stock, min, max, machineID);
                Inventory.updatePart(index, newPart);
            }else if (OutsourcedToggle.isSelected()) {
                // Create an Outsourced part and pass in all the fields
                String companyName = modifyPartMachineIDField.getText();
                Outsourced newPart = new Outsourced(index, name, price, stock, min, max, companyName);
                Inventory.updatePart(index, newPart);
            }
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/FirstScreen.fxml")));
            Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Inventory Management System");
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Navigates to FirstScreen (after confirmation)
     *
     * @param actionEvent when the cancel button is pressed
     * @throws IOException from FXMLLoader
     */
    public void onCancel(ActionEvent actionEvent) throws IOException{
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
}
