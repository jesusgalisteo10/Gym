package com.example.test;

import com.example.model.connection.ConnectionProperties;
import com.example.utils.XMLManager;

public class saveConnection {
    public static void main(String[] args) {
        ConnectionProperties c = new ConnectionProperties("localhost","3306","gymgenius","root","root");
        XMLManager.writeXML(c,"connection.xml");
    }
}
