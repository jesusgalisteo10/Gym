package com.example.view;

import com.example.App;
import com.example.model.dao.MachineDAO;
import com.example.model.entity.Client;
import com.example.model.entity.Machine;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientMachineController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView exitButton;
    @FXML
    private TableView<Machine> tableMachine;
    @FXML
    private TableColumn<Machine, String> colMachine;
    @FXML
    private TableColumn<Machine, Integer> colCodeMachine;
    @FXML
    private TableColumn<Machine, Integer> colRoom;
    @FXML
    private Text textName;
    private ObservableList<Machine> machineList;
    private Client client;
    /**
     * Initializes and manages the display of machines associated with a specific client.
     * Displays a table view of machines assigned to the client, including machine code, type, and room.
     * Allows navigation back to the main page and dynamically updates client information.
     *
     * @param input the input data (Client object) passed to initialize the controller
     * @throws Exception if there's an issue during database operations or client information retrieval
     */
    @Override
    public void onOpen(Object input) throws Exception {
        client = (Client) input;
        showName();

        // Retrieve machines associated with the client from the database
        List<Machine> machines = MachineDAO.findByCode(client.getCode());
        this.machineList = FXCollections.observableArrayList(machines);
        tableMachine.setItems(this.machineList);
    }
    @Override
    public void onClose(Object output) {
    }

    /**
     * Updates client name display on the UI.
     */
    private void showName() {
        textName.setText(client.getName() + " " + client.getSurname());
    }

    /**
     * Navigates back to the main page of the application.
     * Throws an exception if there's an issue during scene change.
     *
     * @throws Exception if there's an issue during scene change
     */
    @FXML
    private void goBack() throws Exception {
        App.currentController.changeScene(Scenes.MAINPAGE, null);
    }

    /**
     * Initializes table columns and their respective cell value factories for displaying machine details.
     * Configures columns for machine code, type, and associated room.
     *
     * @param location the location used to resolve relative paths for the root object
     * @param resources the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCodeMachine.setCellValueFactory(machine -> new SimpleIntegerProperty(machine.getValue().getCode()).asObject());
        colMachine.setCellValueFactory(machine -> new SimpleStringProperty(machine.getValue().getMachineType()));
        colRoom.setCellValueFactory(machine -> new SimpleIntegerProperty(machine.getValue().getRoom().getCode()).asObject());
    }

}
