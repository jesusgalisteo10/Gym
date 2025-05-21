package com.example.model.dao;

import com.example.model.connection.ConnectionMariaDB;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Client_MachineDAO {
    // SQL query to insert a client-machine relationship into the database
    private static final String INSERTCM = "INSERT INTO client_machine (ClientCode, MachineCode) VALUES (?, ?)";
    // SQL query to delete a client-machine relationship from the database
    private static final String DELETEMC = "DELETE FROM client_machine WHERE ClientCode = ? AND MachineCode = ?;";

    // Empty constructor that initializes an instance of Client_MachineDAO
    public Client_MachineDAO() {
    }

    /**
     * Creates and returns a new instance of Client_MachineDAO.
     *
     * @return new instance of Client_MachineDAO
     */
    public static Client_MachineDAO build() {
        return new Client_MachineDAO();
    }

    /**
     * Inserts a relationship between a machine and a client into the client_machine table.
     *
     * @param machineCode the code of the machine to insert
     * @param clientCode  the code of the client to insert
     */
    public static void insertMachineToClient(int machineCode, int clientCode) {
        // Use a try-with-resources to ensure that the PreparedStatement is closed automatically
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(INSERTCM)) {
            // Set the parameters for the query
            pst.setInt(1, machineCode);
            pst.setInt(2, clientCode);
            // Execute the insert query
            pst.executeUpdate();
        } catch (SQLException e) {
            // Specific exception handling for duplicate entries
            if (e.getSQLState().equals("23000") && e.getErrorCode() == 1062) {
                // If a duplicate entry is found, show an alert to the user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error: Duplicate entry found.");
                alert.show();
            } else {
                // For other errors, print the stack trace
                e.printStackTrace();
            }
        }
    }

    /**
     * Deletes a relationship between a client and a machine from the client_machine table.
     *
     * @param clientCode  the code of the client whose relationship is to be deleted
     * @param machineCode the code of the machine whose relationship is to be deleted
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean deleteMachineFromClient(int clientCode, int machineCode) throws SQLException {
        // Use a try-with-resources to ensure that the PreparedStatement is closed automatically
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(DELETEMC)) {
            // Set the parameters for the query
            pst.setInt(1, clientCode);
            pst.setInt(2, machineCode);
            // Execute the delete query and get the number of affected rows
            int rowsAffected = pst.executeUpdate();
            // Return true if any rows were affected, indicating that the deletion was successful
            return rowsAffected > 0;
        }
    }
}
