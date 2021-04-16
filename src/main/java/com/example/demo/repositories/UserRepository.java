package com.example.demo.repositories;

import com.example.demo.models.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class UserRepository {

    private static String url;
    private static String user;
    private static String password;

    private Connection conn;

    public UserRepository() {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/application.properties"));
            user = prop.getProperty("user");
            password = prop.getProperty("password");
            url = prop.getProperty("url");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //check if user exists
    //if show wishlist
    //if not, make new user

    public int doesUserExist(String username) {
        Map<Integer, User> userList = getUserMap();

        for (int i = 1; i < userList.size(); i++) {
            if (userList.get(i).getUsername().equals(username)) {
                return userList.get(i).getUserID();
            }
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

        Map<Integer, User> userList = getUserMap();
        return userList.get(userList.size()).getUserID();
    }


    private Map<Integer, User> getUserMap() {
        //  ArrayList<User> userList = new ArrayList<>(); //TODO convert to hashmap
        Map<Integer, User> userMap = new HashMap<>();

        try {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM user");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userID = resultSet.getInt(1);
                String username = resultSet.getString(2);

                User tempUser = new User(userID, username);
                userMap.put(userID, tempUser);
                // userList.add(tempUser);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return userMap;
        //return userList;
    }

}
