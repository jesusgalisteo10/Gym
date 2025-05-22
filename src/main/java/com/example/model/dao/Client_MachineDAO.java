package com.example.model.dao;

import com.example.model.connection.ConnectionBD;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Client_MachineDAO {
    //  // Consulta SQL para insertar una relación cliente-máquina en la base de datos
    private static final String INSERTCM = "INSERT INTO client_machine (ClientCode, MachineCode) VALUES (?, ?)";
    //  // Consulta SQL para eliminar una relación cliente-máquina de la base de datos
    private static final String DELETEMC = "DELETE FROM client_machine WHERE ClientCode = ? AND MachineCode = ?;";

    // // Constructor vacío que inicializa una instancia de Client_MachineDAO
    public Client_MachineDAO() {
    }

    /**
     * Crea y devuelve una nueva instancia de Client_MachineDAO.
     *
     * @return nueva instancia de Client_MachineDAO
     */
    public static Client_MachineDAO build() {
        return new Client_MachineDAO();
    }

    /**
     * Inserta una relación entre una máquina y un cliente en la tabla client_machine.
     *
     * @param machineCode el código de la máquina a insertar
     * @param clientCode  el código del cliente a insertar
     */
    public static void insertMachineToClient(int machineCode, int clientCode) {
        // Usa un try-with-resources para asegurar que el PreparedStatement se cierre automáticamente
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(INSERTCM)) {
            // Establece los parámetros para la consulta
            pst.setInt(1, machineCode);
            pst.setInt(2, clientCode);
            // Ejecuta la consulta de inserción
            pst.executeUpdate();
        } catch (SQLException e) {
            // Manejo de excepciones específico para entradas duplicadas
            if (e.getSQLState().equals("23000") && e.getErrorCode() == 1062) {
                //Si se encuentra una entrada duplicada, muestra una alerta al usuario
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Error: Duplicate entry found.");
                alert.show();
            } else {
                //  Para otros errores, imprime la traza de la pila
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
        try (PreparedStatement pst = ConnectionBD.getConnection().prepareStatement(DELETEMC)) {
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
