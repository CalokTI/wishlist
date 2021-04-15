package com.example.demo.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseWriter {

    private static String url;
    private static String user;
    private static String password;

    public DatabaseWriter(){
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/application.properties"));
            user = prop.getProperty("user");
            password = prop.getProperty("password");
            url = prop.getProperty("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertWish(int userID, String description, String price) {
        String sql = "INSERT INTO wish(userID, description, price, isReserved) VALUES(?,?,?,0)";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,userID);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, price);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
