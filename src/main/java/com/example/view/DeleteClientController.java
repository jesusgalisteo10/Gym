package com.example.view;

import com.example.App;
import com.example.model.dao.ClientDAO;
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

public class DeleteClientController extends Controller implements Initializable {
    @FXML
    private Button deleteClientButton;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField fieldClientCode;
    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    /**
     * Initializes the controller upon FXML loading.
     * Sets up the delete client button action to handle client deletion.
     * Deletes a client from the database based on the entered client code.
     * Shows alerts for various scenarios such as empty input, invalid client code format,
     * successful deletion, or deletion failure.
     *
     * @param location the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resources the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deleteClientButton.setOnAction(event -> {
            try {
                deleteClient(event);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Handles the delete client action when triggered by the delete client button.
     * Validates and deletes a client from the database based on the entered client code.
     * Shows appropriate error messages for invalid input, invalid client code format,
     * successful deletion, or deletion failure.
     *
     * @param event the event triggered by the delete client action
     * @throws SQLException if there's an issue during database operations
     */
    @FXML
    private void deleteClient(Event event) throws SQLException {
        String clientCode = fieldClientCode.getText().trim();

        // Check if client code field is empty
        if (clientCode.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "El campo del código del cliente no puede estar vacío.");
        } else {
            try {
                // Check if client code contains only digits
                if (!clientCode.matches("\\d+")) {
                    showAlert(Alert.AlertType.ERROR, "Error", "El código del cliente debe contener solo números.");
                    return;
                }
                ClientDAO cdao = new ClientDAO();
                boolean deleted = cdao.delete(Integer.parseInt(clientCode));

                // Display appropriate alert based on deletion result
                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "El cliente se ha eliminado correctamente.");
                    App.currentController.changeScene(Scenes.MAINPAGE, null);
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "El código del cliente no coincide con ningún cliente en la base de datos.");
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "El código del cliente debe ser un número válido.");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar al cliente de la base de datos.");
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
