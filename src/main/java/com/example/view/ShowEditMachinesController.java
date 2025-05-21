package com.example.view;

import com.example.App;
import com.example.model.dao.MachineDAO;
import com.example.model.entity.Machine;
import com.example.model.entity.Room;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ShowEditMachinesController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView exitButton;
    @FXML
    private Button addMachine;
    @FXML
    private Button addRoom;
    @FXML
    private Button deleteMachine;
    @FXML
    private Button deleteRoom;
    @FXML
    private TableView<Machine> tableMachine;
    @FXML
    private TableColumn<Machine, String> colMachine;
    @FXML
    private TableColumn<Machine, Integer> colCodeMachine;
    @FXML
    private TableColumn<Machine, Integer> colRoom;
    private ObservableList<Machine> machineList;


    /**
     * Initializes the view associated with this controller when it is opened.
     * Retrieves all machines from the database and populates them into the table view.
     *
     * @param input Object input (typically not used directly in this context).
     * @throws Exception If an error occurs while retrieving machines from the database.
     */
    @Override
    public void onOpen(Object input) throws Exception {
        // Retrieve all machines from the database
        List<Machine> machines = MachineDAO.findAll();

        // Convert the list of machines into an observable list for JavaFX
        this.machineList = FXCollections.observableArrayList(machines);

        // Set the observable list as the items to display in the table view (tableMachine)
        tableMachine.setItems(this.machineList);
    }


    @Override
    public void onClose(Object output) {

    }

    /**
     * Navigates back to the main page.
     *
     * @throws Exception If an error occurs while changing the scene to the main page.
     */
    @FXML
    public void goBack() throws Exception {
        App.currentController.changeScene(Scenes.MAINPAGE, null);
    }


    /**
     * Initializes the controller when the associated view is loaded.
     * Sets up the table view for machines with editable columns.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Make the table view editable
        tableMachine.setEditable(true);

        // Set up column for machine code
        colCodeMachine.setCellValueFactory(machine -> new SimpleIntegerProperty(machine.getValue().getCode()).asObject());

        // Set up column for room code
        colRoom.setCellValueFactory(machine -> new SimpleIntegerProperty(machine.getValue().getRoom().getCode()).asObject());
        colRoom.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colRoom.setOnEditCommit(event -> {
            try {
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                Machine machine = MachineDAO.findByMachineCode(event.getRowValue().getCode());
                Room room = machine.getRoom();
                if (event.getNewValue().longValue() <= 50) {
                    room.setCode(event.getNewValue());
                    MachineDAO.build().update(machine);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, the field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException | SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, the entered value is not a valid number");
                alert.show();
            }
        });

        // Set up column for machine type
        colMachine.setCellValueFactory(machine -> new SimpleStringProperty(machine.getValue().getMachineType()));
        colMachine.setCellFactory(TextFieldTableCell.forTableColumn());
        colMachine.setOnEditCommit(event -> {
            try {
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }

                if (event.getNewValue().length() <= 60) {
                    Machine machine = event.getRowValue();
                    machine.setMachineType(event.getNewValue());
                    MachineDAO.build().update(machine);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, the field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException | SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, the entered value is not a valid number");
                alert.show();
            }
        });
    }


    /**
     * Opens a modal dialog to add a new room.
     *
     * @throws Exception If an error occurs while opening the modal dialog.
     */
    public void openAddRoom() throws Exception {
        App.currentController.openModal(Scenes.ADDROOM, "Adding room...", this, null);
    }

    /**
     * Opens a modal dialog to add a new machine.
     *
     * @throws Exception If an error occurs while opening the modal dialog.
     */
    public void openAddMachine() throws Exception {
        App.currentController.openModal(Scenes.ADDMACHINE, "Adding machine...", this, null);
    }

    /**
     * Opens a modal dialog to delete an existing machine.
     *
     * @throws Exception If an error occurs while opening the modal dialog.
     */
    public void openDeleteMachine() throws Exception {
        App.currentController.openModal(Scenes.DELETEMACHINE, "Deleting machine...", this, null);
    }

    /**
     * Opens a modal dialog to delete an existing room.
     *
     * @throws Exception If an error occurs while opening the modal dialog.
     */
    public void openDeleteRoom() throws Exception {
        App.currentController.openModal(Scenes.DELETEROOM, "Deleting room...", this, null);
    }



}
