package com.zept.practicetool.zapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author Allen James Laxamana
 */
public class Database extends Connect {
    
    public Database() {
        super(); // Call the constructor of the parent class
        createNewTable(); // Create a new database if there is none
    }

    // Method for inserting records
    public void insertUser(String firstName, String middleName, String lastName, String email, String password) {
        String sql = "INSERT OR IGNORE INTO user (firstName, middleName, lastName, email, password) VALUES(?,?,?,?,?)"
                + "ON CONFLICT(email) DO NOTHING";
        try {
            Connection con = connect();
            PreparedStatement insert = con.prepareStatement(sql);
            insert.setString(1, firstName);
            insert.setString(2, middleName);
            insert.setString(3, lastName);
            insert.setString(4, email);
            insert.setString(5, password);
            insert.executeUpdate();

            /** CHECK THE CONTENTS OF THE TABLE
            String query = "SELECT * FROM user";
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            System.out.println("Contents of the 'user' table:");
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = resultSet.getString(columnName);
                    System.out.println(columnName + ": " + value);
                }
                System.out.println();
            }
            **/
        } catch (SQLException e) {
            if (e != null && "23000".equals(e.getSQLState())) {
                System.out.println("Error: Email address already exists in the database");
            } else {
                System.out.println(e.getMessage());
            }
            // JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            closeConnection();
        }
    }
    
    public boolean checkLogin(String email, String pass) {
        
        String query = "SELECT * FROM user WHERE email = ? AND password = ?";
        
        try (Connection connection = connect();
            PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pass);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();  // If there's a match, resultSet will have at least one row.

        } 
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
