package com.example.view;

import com.example.model.dao.ClientDAO;
import com.example.model.dao.Client_MachineDAO;
import com.example.model.dao.MachineDAO;
import com.example.model.entity.Client;
import com.example.model.entity.Machine;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class DeleteMachineToClientController extends Controller implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button deleteMachineFromClientButton;
    @FXML
    private ComboBox<String> machineComboBox;
    @FXML
    private ComboBox<String> clientComboBox;
    @FXML
    private ImageView goBackButton;

    private Map<String, String> machinesMap;
    private  Map<String, String> clientsMap;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }
    /**
     * Initializes the controller upon FXML loading.
     * Retrieves all clients and machines from the database to populate the client and machine combo boxes.
     * Maps client codes to client names and machine codes to machine types for combo box display.
     * Sets up event handling for deleting a machine from a client.
     * Shows alerts for successful deletion, assignment error, or database operation failure.
     *
     * @param location  the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resources the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate client combo box with client names
        List<Client> clients = ClientDAO.build().findAll();
        clientsMap = new HashMap<>();
        for (Client client : clients) {
            clientsMap.put(String.valueOf(client.getCode()), client.getName());
        }
        clientComboBox.setItems(FXCollections.observableArrayList(clientsMap.values()));

        // Populate machine combo box with machine types
        List<Machine> machines;
        try {
            machines = MachineDAO.findAll();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        machinesMap = new HashMap<>();
        for (Machine machine : machines) {
            machinesMap.put(String.valueOf(machine.getCode()), machine.getMachineType());
        }
        machineComboBox.setItems(FXCollections.observableArrayList(machinesMap.values()));
    }

    /**
     * Handles the action of deleting a machine from a client.
     * Retrieves selected machine and client codes from combo boxes, validates them,
     * and deletes the association from the database.
     * Displays appropriate alert messages for success, assignment error, or database operation failure.
     */
    @FXML
    private void deleteMachineFromClient() {
        String codeMachine = "";
        String valueMachine = machineComboBox.getValue();
        for (String key : machinesMap.keySet()) {
            if (machinesMap.get(key).equals(valueMachine)) {
                codeMachine = key;
                break;
            }
        }

        String codeClient = "";
        String valueClient = clientComboBox.getValue();
        for (String key : clientsMap.keySet()) {
            if (clientsMap.get(key).equals(valueClient)) {
                codeClient = key;
                break;
            }
        }

        int machineCode = 0;
        int clientCode = 0;
        if (!codeMachine.isEmpty() && codeMachine.matches("\\d+") && !codeClient.isEmpty() && codeClient.matches("\\d+")) {
            machineCode = Integer.parseInt(codeMachine);
            clientCode = Integer.parseInt(codeClient);

            try {
                boolean deleted = Client_MachineDAO.deleteMachineFromClient(clientCode, machineCode);
                if (deleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Éxito", "La máquina se ha eliminado del cliente correctamente.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "La máquina no estaba asignada a este cliente.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Error al eliminar la máquina del cliente.");
                e.printStackTrace();
            }

        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No se ha seleccionado nada en la base de datos.");
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
