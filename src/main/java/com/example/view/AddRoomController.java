package com.example.view;

import com.example.App;
import com.example.model.dao.RoomDAO;
import com.example.model.entity.Room;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AddRoomController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField fieldRoom;
    @FXML
    private Button addRoomButton;

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
     * Validates and saves a new room code, ensuring it's not empty and within valid range.
     * Checks if the room code already exists in the database before saving.
     * Shows appropriate error messages for invalid input or existing room codes.
     *
     * @param event the event triggered by the save action
     * @throws Exception if there's an issue during database operations or parsing room code
     */
    public void onSave(Event event) throws Exception {
        String roomCodeText = fieldRoom.getText().trim();

        // Check if room code field is empty
        if (roomCodeText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "El campo no puede estar vacío.");
        } else {
            try {
                int roomCode = Integer.parseInt(roomCodeText);

                // Check if room code is within valid range
                if (roomCode > 9999) {
                    showAlert(Alert.AlertType.ERROR, "Error", "El valor no puede ser mayor que 9999.");
                } else {
                    RoomDAO roomDAO = new RoomDAO();

                    // Check if room code already exists in the database
                    if (roomDAO.findByRoomCode(roomCode) != null) {
                        showAlert(Alert.AlertType.ERROR, "Error", "El código de habitación ya existe en la base de datos.");
                    } else {
                        // Save new room if it doesn't exist
                        Room room = new Room(roomCode);
                        roomDAO.save(room);
                        // Navigate to show machines page and close current window
                        App.currentController.changeScene(Scenes.SHOWMACHINES, null);
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                    }
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "El valor ingresado no es válido.");
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
