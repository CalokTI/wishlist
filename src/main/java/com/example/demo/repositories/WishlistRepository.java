package com.example.demo.repositories;

import com.example.demo.models.Wish;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class WishlistRepository {

    private static String url;
    private static String user;
    private static String password;

    public WishlistRepository(){
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

    public ArrayList<Wish> getSingleUserWishlist(int userID){

        ArrayList<Wish> wishList = new ArrayList<>();
        ArrayList<Wish> allWishList = getWishlist();

        for (int i = 0; i < allWishList.size(); i++) {
            if (allWishList.get(i).getUserID() == userID){
                wishList.add(allWishList.get(i));
            }
        }
        return wishList;
    }

    private ArrayList<Wish> getWishlist(){ //todo add cache/ttl

        ArrayList<Wish> wishlist = new ArrayList<>();

        try{

            Connection conn = DriverManager.getConnection(url, user, password);

            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM wish");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int wishID = resultSet.getInt(1);
                int userID = resultSet.getInt(2);
                String description = resultSet.getString(3);
                String price = resultSet.getString(4);
                boolean isReserved  = resultSet.getBoolean(5);

                Wish tempWish = new Wish(wishID, userID, description, price, isReserved);
                wishlist.add(tempWish);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return wishlist;
    }


    public void deleteWish(int wishID){ //lack of better position
        String sql = "DELETE FROM wish WHERE wishID = ?";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, wishID);
            preparedStatement.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
