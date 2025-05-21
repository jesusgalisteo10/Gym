package com.example.test;

import com.example.model.dao.ClientDAO;
import com.example.model.entity.Client;

import java.sql.SQLException;

public class TestInsert {
    public static void main(String[] args) throws SQLException {
        Client c = new Client("Franfarsu","Furias","a@a.com","123456","12345678","Macho");
        ClientDAO.build().save(c);
    }

}
