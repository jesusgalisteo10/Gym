package com.example.view;

import com.example.App;
import com.example.model.dao.ClientDAO;
import com.example.model.entity.Client;
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

public class AddClientController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button addClientButton;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldSurname;
    @FXML
    private TextField fieldDni;
    @FXML
    private TextField fieldEmail;
    @FXML
    private TextField fieldPassword;
    @FXML
    private TextField fieldSex;


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
     * Validates input fields (name, surname, DNI, email, password, sex) and displays error messages if any field is empty
     * or if DNI or email formats are invalid. If all validations pass, creates a new Client object and saves it using
     * ClientDAO. Shows a success message upon successful client addition and navigates back to the main page.
     * Closes the current window after successful operation.
     *
     * @param event the event triggered by the save action
     */
    @FXML
    public void onSave(Event event) {
        try {
            String name = fieldName.getText().trim();
            String surname = fieldSurname.getText().trim();
            String dni = fieldDni.getText().trim();
            String email = fieldEmail.getText().trim();
            String password = fieldPassword.getText().trim();
            String sex = fieldSex.getText().trim();

            // Check if any field is empty
            if (name.isEmpty() || surname.isEmpty() || dni.isEmpty() || email.isEmpty() || password.isEmpty() || sex.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Campos Vacíos", "Por favor, complete todos los campos.");
            } else {
                // Validate DNI format
                if (!validateDNI(dni)) {
                    showAlert(Alert.AlertType.ERROR, "Error en DNI", "El formato del DNI no es válido.");
                } else {
                    // Validate email format
                    if (!validateEmail(email)) {
                        showAlert(Alert.AlertType.ERROR, "Error en Correo Electrónico", "El formato del correo electrónico no es válido.");
                    } else {
                        // Create Client object and save to database
                        Client client = new Client(name, surname, email, password, dni, sex);
                        ClientDAO cdao = new ClientDAO();
                        cdao.save(client);

                        // Show success message and navigate to main page
                        showAlert(Alert.AlertType.INFORMATION, "Cliente Agregado", "El cliente se ha agregado correctamente.");
                        App.currentController.changeScene(Scenes.MAINPAGE, null);

                        // Close current window
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error al Guardar", "Hubo un error al intentar guardar el cliente.");
        }
    }


    /**
     * Validates an email address using a regular expression.
     *
     * @param email the email address to validate
     * @return true if the email address is valid, false otherwise
     */
    private boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * Validates a Spanish DNI (National Identity Document) format.
     *
     * @param dni the DNI number to validate
     * @return true if the DNI format is valid, false otherwise
     */
    private boolean validateDNI(String dni) {
        String dniRegex = "\\d{8}[a-zA-Z]";
        return dni.matches(dniRegex);
    }

    /**
     * Displays an alert dialog with specified alert type, title, and content.
     *
     * @param alertType the type of alert (e.g., error, information)
     * @param title the title text of the alert
     * @param content the content text of the alert
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



}