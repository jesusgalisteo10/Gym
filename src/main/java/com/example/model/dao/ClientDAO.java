package com.example.model.dao;

import com.example.model.connection.ConnectionMariaDB;
import com.example.model.entity.Client;
import com.example.model.entity.Machine;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO  implements DAO<Client>{
    private static final String FINDALL = "SELECT * FROM client";
    private static final String FINDBYCODE = "SELECT * FROM client WHERE Clientcode=?";
    private static final String INSERT = "INSERT INTO client ( Name, Surname, Email, Password, DNI, Sex) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE = "UPDATE client SET Name=?, Surname=?, Email=?, Password=?, DNI=?, Sex=? WHERE ClientCode=?";
    private static final String DELETE = "DELETE FROM client WHERE ClientCode=?";

    // Empty constructor
    public ClientDAO() {
    }

    /**
     * Saves a new client entity to the database.
     *
     * @param entity the client entity to save
     * @return the saved client entity
     */
    @Override
    public Client save(Client entity) {
        Client result = new Client();
        if (entity == null || entity.getCode() != 0) return result;
        try {
            if (findByClientCode(entity.getCode()) == null) {
                // INSERT CLIENT
                try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(INSERT)) {
                    pst.setString(1, entity.getName());
                    pst.setString(2, entity.getSurname());
                    pst.setString(3, entity.getEmail());
                    pst.setString(4, hashPassword(entity.getPassword()));
                    pst.setString(5, entity.getDni());
                    pst.setString(6, entity.getSex());
                    pst.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Hashes a password using SHA-256 algorithm.
     *
     * @param password the password to hash
     * @return the hashed password
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates an existing client entity in the database.
     *
     * @param entity the client entity to update
     * @return the updated client entity
     */
    public Client update(Client entity) {
        Client result = new Client();
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(UPDATE)) {
            pst.setString(1, entity.getName());
            pst.setString(2, entity.getSurname());
            pst.setString(3, entity.getEmail());
            pst.setString(4, entity.getPassword());
            pst.setString(5, entity.getDni());
            pst.setString(6, entity.getSex());
            pst.setString(7, String.valueOf(entity.getCode()));
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Creates and returns a new instance of ClientDAO.
     *
     * @return new instance of ClientDAO
     */
    public static ClientDAO build() {
        return new ClientDAO();
    }

    /**
     * Deletes a client from the database by their code.
     *
     * @param clientCode the code of the client to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    @Override
    public boolean delete(int clientCode) throws SQLException {
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(DELETE)) {
            pst.setInt(1, clientCode);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Finds a client by their code.
     *
     * @param code the code of the client to find
     * @return the found client or null if no client was found
     */
    public static Client findByClientCode(Integer code) {
        Client result = null;
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(FINDBYCODE)) {
            pst.setInt(1, code);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    Client c = new ClientLazy();
                    c.setCode(res.getInt("ClientCode"));
                    c.setName(res.getString("Name"));
                    c.setSurname(res.getString("Surname"));
                    c.setEmail(res.getString("Email"));
                    c.setPassword(res.getString("Password"));
                    c.setDni(res.getString("DNI"));
                    c.setSex(res.getString("Sex"));
                    result = c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Finds all clients in the database.
     *
     * @return a list of all clients
     */
    public static List<Client> findAll() {
        List<Client> result = new ArrayList<>();
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(FINDALL)) {
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    Client c = new ClientLazy();
                    c.setCode(res.getInt("ClientCode"));
                    c.setName(res.getString("Name"));
                    c.setSurname(res.getString("Surname"));
                    c.setEmail(res.getString("Email"));
                    c.setPassword(res.getString("Password"));
                    c.setDni(res.getString("DNI"));
                    c.setSex(res.getString("Sex"));
                    result.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}

class ClientLazy extends Client {
    private static final String FINDMACHINESBYCLIENTS = "SELECT * FROM machine m, client_machine cm, client c WHERE m.MachineCode = cm.MachineCode AND cm.ClientCode = c.ClientCode AND c.ClientCode = ?;";

    public ClientLazy() {
    }

    public ClientLazy(String name, String surname, String email, String password, String dni, String sex) {
        super(name, surname, email, password, dni, sex);
    }

    /**
     * Gets the machines associated with the client.
     *
     * @return a list of machines associated with the client
     */
    @Override
    public List<Machine> getMachines() {
        if (super.getMachines() == null) {
            List<Machine> result = new ArrayList<>();
            try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(FINDMACHINESBYCLIENTS)) {
                pst.setInt(1, getCode());
                try (ResultSet res = pst.executeQuery()) {
                    while (res.next()) {
                        Client c = new Client();
                        c.setCode(res.getInt("ClientCode"));
                        c.setName(res.getString("Name"));
                        c.setSurname(res.getString("Surname"));
                        c.setEmail(res.getString("Email"));
                        c.setPassword(res.getString("Password"));
                        c.setDni(res.getString("DNI"));
                        c.setSex(res.getString("Sex"));
                        result.add(c);
                    }
                }
                super.setMachines(result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return super.getMachines();
    }
}
