package com.example.model.dao;

import com.example.model.connection.ConnectionMariaDB;
import com.example.model.entity.Room;
import com.example.model.entity.Machine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private static final String INSERT = "INSERT INTO room (RoomCode) VALUES (?)";
    private static final String UPDATE = "UPDATE room SET RoomCode=? WHERE RoomCode=?";
    private static final String FINDALL = "SELECT RoomCode FROM room";
    private static final String FINDBYCODE = "SELECT RoomCode FROM room WHERE RoomCode=?";
    private static final String DELETE = "DELETE FROM room WHERE RoomCode=?";
    private static final String FINDMACHINESBYROOM = "SELECT * FROM Machine WHERE RoomCode = ?;";

    /**
     * Saves a new room entity to the database.
     *
     * @param entity the room entity to save
     * @return the saved room entity
     */
    public static Room save(Room entity) {
        Room result = entity;
        if (entity == null || entity.getCode() == 0) return result;
        try {
            if (findByRoomCode(entity.getCode()) == null) {
                // INSERT
                try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
                    pst.setInt(1, entity.getCode());
                    pst.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Updates an existing room entity in the database.
     *
     * @param entity the room entity to update
     * @return the updated room entity
     */
    public Room update(Room entity) {
        Room result = new Room();
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(UPDATE)) {
            pst.setInt(1, entity.getCode());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Deletes a room from the database by its code.
     *
     * @param roomCode the code of the room to delete
     * @return true if the deletion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public static boolean delete(int roomCode) throws SQLException {
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(DELETE)) {
            pst.setInt(1, roomCode);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Finds a room by its code.
     *
     * @param code the code of the room to find
     * @return the found room or null if no room was found
     */
    public static Room findByRoomCode(Integer code) {
        Room result = null;
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(FINDBYCODE)) {
            pst.setInt(1, code);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                result = new Room();
                result.setCode(res.getInt("RoomCode"));

                // Fetch machines associated with the room
                ArrayList<Machine> machines = new ArrayList<>();
                try (PreparedStatement pstMachine = ConnectionMariaDB.getConnection().prepareStatement(FINDMACHINESBYROOM)) {
                    pstMachine.setInt(1, code);
                    ResultSet resMachines = pstMachine.executeQuery();
                    while (resMachines.next()) {
                        Machine machine = new Machine();
                        machine.setCode(resMachines.getInt("MachineCode"));
                        machine.setMachineType(resMachines.getString("MachineType"));
                        machines.add(machine);
                    }
                    resMachines.close();
                }
                result.setMachines(machines);
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Finds all rooms in the database.
     *
     * @return a list of all rooms
     */
    public static List<Room> findAll() {
        List<Room> result = new ArrayList<>();
        try (PreparedStatement pst = ConnectionMariaDB.getConnection().prepareStatement(FINDALL)) {
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                Room room = new Room();
                room.setCode(res.getInt("RoomCode"));
                result.add(room);
            }
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Creates and returns a new instance of RoomDAO.
     *
     * @return new instance of RoomDAO
     */
    public static RoomDAO build() {
        return new RoomDAO();
    }
}
