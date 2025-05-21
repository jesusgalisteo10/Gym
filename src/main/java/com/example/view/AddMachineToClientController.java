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


public class AddMachineToClientController extends Controller implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button addMachineToClientButton;
    @FXML
    private ComboBox<String> machineComboBox;
    @FXML
    private ComboBox<String> clientComboBox;
    @FXML
    private ImageView goBackButton;

    private  Map<String, String> machinesMap;
    private  Map<String, String> clientsMap;

    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    /**
     * Initializes the controller with client and machine data for dropdown menus.
     * Retrieves and populates client and machine dropdowns with data from the database.
     * Handles the assignment of machines to clients based on user selection.
     *
     * @throws Exception if there's an issue during database operations
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Retrieve all clients from database and populate clientComboBox
        List<Client> clients = ClientDAO.build().findAll();
        clientsMap = new HashMap<>();
        for (Client client : clients) {
            clientsMap.put(String.valueOf(client.getCode()), client.getName());
        }
        clientComboBox.setItems(FXCollections.observableArrayList(clientsMap.values()));

        // Retrieve all machines from database and populate machineComboBox
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
     * Assigns the selected machine to the selected client.
     * Retrieves the selected machine and client codes from their respective dropdowns,
     * then inserts the association into the database using Client_MachineDAO.
     * Shows an error message if either machine or client is not selected properly.
     *
     * @throws SQLException if there's an issue during database operation
     */
    @FXML
    private void AssignMachines() throws SQLException {
        String CodeMachine = "";
        String ValueMachine = machineComboBox.getValue();
        for (String key : machinesMap.keySet()) {
            if (machinesMap.get(key).equals(ValueMachine)) {
                CodeMachine = key;
            }
        }

        String CodeClient = "";
        String ValueClient = clientComboBox.getValue();
        for (String key : clientsMap.keySet()) {
            if (clientsMap.get(key).equals(ValueClient)) {
                CodeClient = key;
            }
        }

        int machineCode = 0;
        int clientCode = 0;
        // Validate and parse codes to integers
        if (CodeMachine != null && CodeMachine.matches("\\d+") && CodeClient != null && CodeClient.matches("\\d+")) {
            machineCode = Integer.parseInt(CodeMachine);
            clientCode = Integer.parseInt(CodeClient);

            // Insert the machine-to-client association into the database
            Client_MachineDAO.insertMachineToClient(clientCode, machineCode);

        } else {
            // Show error alert if either machine or client is not selected properly
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No se ha seleccionado nada en la base de datos");
            alert.show();
        }
    }



}

