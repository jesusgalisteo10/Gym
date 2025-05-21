package com.example.view;

import com.example.App;
import com.example.model.dao.MachineDAO;
import com.example.model.dao.RoomDAO;
import com.example.model.entity.Machine;
import com.example.model.entity.Room;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;


public class AddMachineController extends Controller implements Initializable {
    @FXML
    private Button addMachineButton;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldRoom;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    /**
     * Handles the save action when the user clicks the save button.
     * Validates input fields (machine type and room code) and displays error messages if either field is empty or
     * if the room code is invalid (not found in the database). If validations pass, creates a new Machine object
     * associated with the found Room and saves it using MachineDAO. If the Room associated with the Machine does
     * not exist in the database, saves the Room first. Shows a success message upon successful machine addition
     * and navigates to the show machines page. Closes the current window after successful operation.
     *
     * @param event the event triggered by the save action
     * @throws Exception if there's an issue during database operations or parsing room code
     */
    public void onSave(Event event) throws Exception {
        String machineType = fieldName.getText().trim();
        String roomCodeText = fieldRoom.getText().trim();

        // Check if machine type is empty
        if (machineType.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "El campo del tipo de máquina no puede estar vacío.");
        } else if (roomCodeText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "El campo del código de habitación no puede estar vacío.");
        } else {
            try {
                int roomCode = Integer.parseInt(roomCodeText);

                RoomDAO rdao = new RoomDAO();
                Room room = rdao.findByRoomCode(roomCode);

                // Check if room with given code exists
                if (room == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "El código de habitación no se encuentra en la base de datos.");
                } else {
                    // Create Machine object and save to database
                    Machine machine = new Machine(room, machineType);
                    MachineDAO cdao = new MachineDAO();
                    cdao.save(machine);

                    // Save the associated Room if it wasn't found initially
                    if (room == null) {
                        rdao.save(machine.getRoom());
                    }

                    // Navigate to show machines page and close current window
                    App.currentController.changeScene(Scenes.SHOWMACHINES, null);
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "El código de habitación ingresado no es válido.");
            }
        }
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
