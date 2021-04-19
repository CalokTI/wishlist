package com.example.demo.repositories;

import com.example.demo.models.Wish;
import com.example.demo.services.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;

public class WishlistRepository {

    private Connection conn;

    //----- Calls the database connection to get the entrance to the SQL database -----\\
    public WishlistRepository() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        this.conn = databaseConnection.getConn();
    }

    //------ Makes arraylist with wishes based on the SQL data ------\\
    private ArrayList<Wish> getWishlist(){

        ArrayList<Wish> wishlist = new ArrayList<>();

        try{
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM wish");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int wishID = resultSet.getInt(1);
                int userID = resultSet.getInt(2);
                String description = resultSet.getString(3);
                String price = resultSet.getString(4);
                boolean isReserved = resultSet.getBoolean(5);
                int reservedUserID = resultSet.getInt(6);

                Wish tempWish = new Wish(wishID, userID, description, price, isReserved, reservedUserID);
                wishlist.add(tempWish);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return wishlist;
    }

    //----- Makes arraylist with all the wishes from a specific user, by using userID -----\\
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

    //----- Inserts a wish into the SQL database by using userID, description and price -----\\
    public void insertWish(int userID, String description, String price) {
        String sql = "INSERT INTO wish(userID, description, price, isReserved) VALUES(?,?,?,0)";

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1,userID);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, price);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //----- Finds and delete a specific wish from the SQL database by using its wishID-----\\
    public void deleteWish(int wishID){ //lack of better position
        String sql = "DELETE FROM wish WHERE wishID = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, wishID);
            preparedStatement.executeUpdate();
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //----- Updates the value isReserved in the SQL database from 0 to 1 by using wishID and userID -----\\
    public void addReservation(int wishID, int userID){
        String sql = "UPDATE wish SET isReserved = 1, reservedUserID = ? WHERE wishID = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, userID);
            preparedStatement.setInt(2, wishID);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //----- Updates the value isReserved in the SQL database from 1 to 0 by using wishID -----\\
    public void removeReservation(int wishID){
        String sql = "UPDATE wish SET isReserved = 0, reservedUserID = ? WHERE wishID = ?";
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setNull(1, java.sql.Types.INTEGER);
            preparedStatement.setInt(2, wishID);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}
