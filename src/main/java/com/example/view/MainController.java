package com.example.view;

import com.example.App;
import com.example.model.dao.ClientDAO;
import com.example.model.entity.Client;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class MainController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button addClient;
    @FXML
    private Button deleteClient;
    @FXML
    private Button showMachines;
    @FXML
    private Button showMachinesToClient;
    @FXML
    private Button addMachineToClient;
    @FXML
    private Button deleteMachineToClient;
    @FXML
    private TableView<Client> tableInfo;
    @FXML
    private TableColumn<Client, String> colCode;
    @FXML
    private TableColumn<Client, String> colName;
    @FXML
    private TableColumn<Client, String> colSurname;
    @FXML
    private TableColumn<Client, String> colDNI;
    @FXML
    private TableColumn<Client, String> colEmail;
    @FXML
    private TableColumn<Client, String> colSex;
    @FXML
    private ImageView exitButton;
    @FXML
    private TableColumn<?, ?> colDeleteClient;
    @FXML
    private TableColumn<?, ?> colAddMachine;
    @FXML
    private ObservableList<Client> clientList;

    /**
     * Method invoked when the view associated with this controller is opened.
     * Loads all clients from the database and displays them in the table.
     *
     * @param input The input object (typically not directly used in this context).
     * @throws Exception If an error occurs while loading clients from the database.
     */
    @Override
    public void onOpen(Object input) throws Exception {
        // Initialize the DAO to access client data
        ClientDAO cdao = new ClientDAO();

        // Retrieve all clients from the database
        List<Client> clients = cdao.findAll();

        // Convert the list of clients to an observable list for JavaFX
        this.clientList = FXCollections.observableArrayList(clients);

        // Set the observable list as the items to display in the table (tableInfo)
        tableInfo.setItems(this.clientList);
    }


    @Override
    public void onClose(Object output) {

    }

    /**
     * Validates if a DNI number has the correct format.
     *
     * @param dni The DNI number to validate.
     * @return true if the DNI has the correct format, false otherwise.
     */
    private boolean validateDNI(String dni) {
        // Regular expression to validate DNI: 8 digits followed optionally by a letter
        String dniPattern = "\\d{8}[a-zA-Z]";
        return dni.matches(dniPattern);
    }

    /**
     * Validates if an email address has the correct format.
     *
     * @param email The email address to validate.
     * @return true if the email address has the correct format, false otherwise.
     */
    private boolean validateEmail(String email) {
        // Regular expression to validate email address
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailPattern);
    }


    /**
     * Initializes the controller.
     * Sets up table columns for editing client information and handles edit commit events.
     *
     * @param location  The location used to resolve relative paths for the root object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Enable table editing
        tableInfo.setEditable(true);

        // Set row factory to handle double-click events on rows
        tableInfo.setRowFactory(tv -> {
            TableRow<Client> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                Client client = tableInfo.getSelectionModel().getSelectedItem();
                if (event.getClickCount() == 3 && (!row.isEmpty())) {
                    try {
                        App.currentController.changeScene(Scenes.MACHINESTOCLIENT, client);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            return row;
        });

        // Configure column 'Code' for client code
        colCode.setCellValueFactory(client -> new SimpleStringProperty(String.valueOf(client.getValue().getCode())));
        colCode.setOnEditCommit(event -> {
            try {
                // Check if new value is different from old value
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                // Validate length of new value
                if (event.getNewValue().length() <= 60) {
                    Client client = event.getRowValue();
                    client.setCode(Integer.parseInt(event.getNewValue()));
                    ClientDAO.build().update(client);
                } else {
                    // Display error if length exceeds 60 characters
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException e) {
                // Display error if invalid number format
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, invalid number format entered");
                alert.show();
            }
        });

        // Configure column 'Name' for client name
        colName.setCellValueFactory(client -> new SimpleStringProperty(client.getValue().getName()));
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(event -> {
            try {
                // Check if new value is different from old value
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                // Validate length of new value
                if (event.getNewValue().length() <= 60) {
                    Client client = event.getRowValue();
                    client.setName(event.getNewValue());
                    ClientDAO.build().update(client);
                } else {
                    // Display error if length exceeds 60 characters
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException e) {
                // Display error if invalid number format
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, invalid number format entered");
                alert.show();
            }
        });

        // Configure column 'Surname' for client surname
        colSurname.setCellValueFactory(client -> new SimpleStringProperty(client.getValue().getSurname()));
        colSurname.setCellFactory(TextFieldTableCell.forTableColumn());
        colSurname.setOnEditCommit(event -> {
            try {
                // Check if new value is different from old value
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                // Validate length of new value
                if (event.getNewValue().length() <= 60) {
                    Client client = event.getRowValue();
                    client.setSurname(event.getNewValue());
                    ClientDAO.build().update(client);
                } else {
                    // Display error if length exceeds 60 characters
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException e) {
                // Display error if invalid number format
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, invalid number format entered");
                alert.show();
            }
        });

        // Configure column 'DNI' for client DNI
        colDNI.setCellValueFactory(client -> new SimpleStringProperty(client.getValue().getDni()));
        colDNI.setCellFactory(TextFieldTableCell.forTableColumn());
        colDNI.setOnEditCommit(event -> {
            boolean shouldUpdate = true;
            try {
                String newValue = event.getNewValue().trim();

                // Validate DNI format and length
                if (newValue.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "DNI cannot be empty.");
                    shouldUpdate = false;
                } else if (!validateDNI(newValue)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid DNI format.");
                    shouldUpdate = false;
                } else if (newValue.equals(event.getOldValue())) {
                    shouldUpdate = false;
                } else if (newValue.length() > 9) {
                    showAlert(Alert.AlertType.ERROR, "Error", "DNI cannot exceed 9 characters including the letter.");
                    shouldUpdate = false;
                }

                if (shouldUpdate) {
                    Client client = event.getRowValue();
                    client.setDni(newValue);
                    ClientDAO.build().update(client);
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid value entered.");
                shouldUpdate = false;
            }
        });

        // Configure column 'Email' for client email
        colEmail.setCellValueFactory(client -> new SimpleStringProperty(client.getValue().getEmail()));
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setOnEditCommit(event -> {
            boolean shouldUpdate = true;
            try {
                String newValue = event.getNewValue().trim();

                // Validate email format and length
                if (newValue.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Email cannot be empty.");
                    shouldUpdate = false;
                } else if (!validateEmail(newValue)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid email format.");
                    shouldUpdate = false;
                } else if (newValue.equals(event.getOldValue())) {
                    shouldUpdate = false;
                } else if (newValue.length() > 60) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Email cannot exceed 60 characters.");
                    shouldUpdate = false;
                }

                if (shouldUpdate) {
                    Client client = event.getRowValue();
                    client.setEmail(newValue);
                    ClientDAO.build().update(client);
                }
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid value entered.");
                shouldUpdate = false;
            }
        });

        // Configure column 'Sex' for client sex
        colSex.setCellValueFactory(client -> new SimpleStringProperty(client.getValue().getSex()));
        colSex.setCellFactory(TextFieldTableCell.forTableColumn());
        colSex.setOnEditCommit(event -> {
            try {
                // Check if new value is different from old value
                if (event.getNewValue().equals(event.getOldValue())) {
                    return;
                }
                // Validate length of new value
                if (event.getNewValue().length() <= 60) {
                    Client client = event.getRowValue();
                    client.setSex(event.getNewValue());
                    ClientDAO.build().update(client);
                } else {
                    // Display error if length exceeds 60 characters
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Error, field cannot exceed 50 characters");
                    alert.show();
                }
            } catch (NumberFormatException e) {
                // Display error if invalid number format
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error, invalid number format entered");
                alert.show();
            }
        });
    }


    /**
     * Opens the scene to add a new client by calling the corresponding modal dialog.
     *
     * @throws Exception If there is an error while opening the modal dialog.
     */
    public void openAddClient() throws Exception {
        App.currentController.openModal(Scenes.ADDCLIENT, "Adding client...", this, null);
    }

    /**
     * Opens the scene to delete a client by calling the corresponding modal dialog.
     *
     * @throws Exception If there is an error while opening the modal dialog.
     */
    public void openDeleteClient() throws Exception {
        App.currentController.openModal(Scenes.DELETECLIENT, "Deleting client...", this, null);
    }

    /**
     * Opens the scene to show machines associated with clients.
     *
     * @throws Exception If there is an error while changing the scene.
     */
    public void openShowMachines() throws Exception {
        App.currentController.changeScene(Scenes.SHOWMACHINES, null);
    }

    /**
     * Opens the modal dialog to add a machine to a client.
     *
     * @throws Exception If there is an error while opening the modal dialog.
     */
    public void openAddMachineClient() throws Exception {
        App.currentController.openModal(Scenes.ADDMACHINETOCLIENT, "Adding machine to client...", this, null);
    }

    /**
     * Opens the modal dialog to delete a machine associated with a client.
     *
     * @throws Exception If there is an error while opening the modal dialog.
     */
    public void openDeleteMachineClient() throws Exception {
        App.currentController.openModal(Scenes.DELETEMACHINETOCLIENT, "Deleting associated machine...", this, null);
    }



    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}