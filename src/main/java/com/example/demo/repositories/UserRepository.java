package com.example.demo.repositories;

import com.example.demo.models.User;
import com.example.demo.services.DatabaseConnection;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {

    private Connection conn;

    //----- Calls the database connection to get the entrance to the SQL database -----\\
    public UserRepository() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        this.conn = databaseConnection.getConn();
    }

    //----- Makes a hashmap with users based on the SQL data, by using username as key -----\\
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

    //----- Inserts a user into the SQL database by using username -----\\
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

    //----- Checks if the user exist in the hashmap by using username as key -----\\
    public int doesUserExist(String username) {
        Map<String, User> userList = getUserMap();
        if (userList.containsKey(username)) {
            return userList.get(username).getUserID();
        }
        return -1;
    }

    //----- Checks the size of the map and return its value to get the last user ID -----\\
    public int getLastUserId() {
        //returns id of last created user
        Map<String, User> userList = getUserMap();
        return userList.size();
    }



}