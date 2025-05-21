package com.example.view;

import com.example.App;
import com.example.model.dao.MachineDAO;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DeleteMachineController extends Controller implements Initializable {

    @FXML
    private Button deleteMachineButton;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField fieldMachineCode;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    /**
     * Initializes the controller upon FXML loading.
     * Sets up the delete machine button action to handle machine deletion.
     * Deletes a machine from the database based on the entered machine code.
     * Shows alerts for various scenarios such as empty input, invalid machine code format,
     * successful deletion, or deletion failure.
     *
     * @param location the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resources the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteMachineButton.setOnAction(event -> {
            try {
                deleteMachine(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Handles the delete machine action when triggered by the delete machine button.
     * Validates and deletes a machine from the database based on the entered machine code.
     * Shows appropriate error messages for invalid input, invalid machine code format,
     * successful deletion, or deletion failure.
     *
     * @param event the event triggered by the delete machine action
     * @throws SQLException if there's an issue during database operations
     */
    @FXML
    private void deleteMachine(Event event) throws SQLException {
        String machineCode = fieldMachineCode.getText().trim();

        // Check if machine code field is empty
        if (machineCode.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "El campo del código de máquina no puede estar vacío.");
        } else {
            try {
                // Check if machine code contains only digits
                if (!machineCode.matches("\\d+")) {
                    showAlert(Alert.AlertType.ERROR, "Error", "El código de máquina debe contener solo números.");
                    return;
                }
                MachineDAO mdao = new MachineDAO();
                boolean deleted = mdao.delete(Integer.parseInt(machineCode));

                // Display appropriate alert based on deletion result
                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "La máquina se ha eliminado correctamente.");
                    App.currentController.changeScene(Scenes.SHOWMACHINES, null);
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "El código de máquina no coincide con ninguna máquina en la base de datos.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "El código de máquina debe ser un número válido.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar la máquina de la base de datos.");
                e.printStackTrace();
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
