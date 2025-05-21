package com.example.model.dao;

import com.example.model.connection.ConnectionMariaDB;
import com.example.model.entity.Machine;
import com.example.model.entity.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MachineDAO implements DAO<Machine> {
    private static final String INSERT = "INSERT INTO Machine (RoomCode, MachineType) VALUES (?,?)";
    private static final String UPDATE = "UPDATE Machine SET RoomCode=?, MachineType=? WHERE MachineCode=?";
    private static final String FIND_BY_CODE = "SELECT MachineCode, RoomCode, MachineType FROM Machine WHERE MachineCode=?";
    private static final String DELETE = "DELETE FROM Machine WHERE MachineCode=?";
    private static final String FINDALL = "SELECT * FROM Machine";
    private static final String FINDMACHINEBYCLIENT = "SELECT m.MachineCode, m.MachineType, m.RoomCode FROM Machine m JOIN Client_Machine cm ON m.MachineCode = cm.MachineCode WHERE cm.ClientCode = ?";

    // Empty constructor
    public MachineDAO() {
    }

    /**
     * Saves a new machine entity to the database.
     *
     * @param entity the machine entity to save
     * @return the saved machine entity
     * @throws SQLException if a database access error occurs
     */
    @Override
    public Machine save(Machine entity) throws SQLException {
        if (entity == null) return null;
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, entity.getRoom().getCode());
            pst.setString(2, entity.getMachineType());
            pst.executeUpdate();
            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setCode(generatedKeys.getInt(1));
                }
            }
        }
        return entity;
    }




    /**
     * Updates an existing machine entity in the database.
     *
     * @param entity the machine entity to update
     * @return the updated machine entity
     * @throws SQLException if a database access error occurs
     */
    public Machine update(Machine entity) throws SQLException {
        if (entity == null || entity.getRoom() == null) return null;
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(UPDATE)) {
            pst.setInt(1, entity.getRoom().getCode());
            pst.setString(2, entity.getMachineType());
            pst.setInt(3, entity.getCode());
            pst.executeUpdate();
        }
        return entity;
    }

    /**
     * Deletes a machine from the database by its code.
     *
     * @param machineCode the code of the machine to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    @Override
    public boolean delete(int machineCode) throws SQLException {
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(DELETE)) {
            pst.setInt(1, machineCode);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Finds a machine by its code.
     *
     * @param code the code of the machine to find
     * @return the found machine or null if no machine was found
     * @throws SQLException if a database access error occurs
     */
    public static Machine findByMachineCode(Integer code) throws SQLException {
        Machine result = null;
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(FIND_BY_CODE)) {
            pst.setInt(1, code);
            try (ResultSet res = pst.executeQuery()) {
                if (res.next()) {
                    result = new Machine();
                    result.setCode(res.getInt("MachineCode"));
                    result.setRoom(RoomDAO.findByRoomCode(res.getInt("RoomCode")));
                    result.setMachineType(res.getString("MachineType"));
                }
            }
        }
        return result;
    }

    /**
     * Finds all machines in the database.
     *
     * @return a list of all machines
     * @throws SQLException if a database access error occurs
     */
    public static List<Machine> findAll() throws SQLException {
        List<Machine> result = new ArrayList<>();
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(FINDALL)) {
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    Machine machine = new Machine();
                    machine.setCode(res.getInt("MachineCode"));
                    // Eager fetch the Room entity
                    machine.setRoom(RoomDAO.findByRoomCode(res.getInt("RoomCode")));
                    machine.setMachineType(res.getString("MachineType"));
                    result.add(machine);
                }
            }
        }
        return result;
    }

    /**
     * Finds machines associated with a specific client code.
     *
     * @param clientCode the code of the client
     * @return a list of machines associated with the client
     */
    public static List<Machine> findByCode(int clientCode) {
        List<Machine> machines = new ArrayList<>();
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(FINDMACHINEBYCLIENT)) {
            pst.setInt(1, clientCode);
            try (ResultSet res = pst.executeQuery()) {
                while (res.next()) {
                    Machine m = new Machine();
                    m.setCode(res.getInt("MachineCode"));
                    m.setMachineType(res.getString("MachineType"));
                    Room room = new Room();
                    room.setCode(Integer.parseInt(res.getString("RoomCode")));
                    m.setRoom(room);
                    machines.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return machines;
    }

    /**
     * Creates and returns a new instance of MachineDAO.
     *
     * @return new instance of MachineDAO
     */
    public static MachineDAO build() {
        return new MachineDAO();
    }
}
