package com.example.demo.repositories;

import com.example.demo.models.User;
import com.example.demo.services.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private Connection conn;

    public UserRepository() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        this.conn = databaseConnection.getConn();
    }

    public int doesUserExist(String username) {
        Map<String, User> userList = getUserMap();
        if (userList.containsKey(username)) {
            return userList.get(username).getUserID();
        }
        return -1;
    }

    public void createNewUser(String username) {
        String sql = "INSERT INTO user(username) VALUES(?)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public int getLastUserId() {
        //returns id of last created user
        Map<String, User> userList = getUserMap();
        return userList.size();
    }


    private Map<String, User> getUserMap() {
        Map<String, User> userMap = new HashMap<>();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM user");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userID = resultSet.getInt(1);
                String username = resultSet.getString(2);

                User tempUser = new User(userID, username);
                userMap.put(username, tempUser); //OBS. bruger username som KEY!
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userMap;
    }

}