package com.zept.practicetool.zapper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Allen James Laxamana
 */
public class Database extends Connect {

    public Database() {
        super(); // Call the constructor of the parent class
        createNewTable(); // Create a new database if there is none
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Method for inserting records
    public void insertUser(String firstName, String middleName, String lastName, String email, String password) {
        String hashedPassword = hashPassword(password);
        
        String sql = "INSERT OR IGNORE INTO user (firstName, middleName, lastName, email, password) VALUES(?,?,?,?,?)"
                + "ON CONFLICT(email) DO NOTHING";
        try {
            Connection con = connect();
            PreparedStatement insert = con.prepareStatement(sql);
            insert.setString(1, firstName);
            insert.setString(2, middleName);
            insert.setString(3, lastName);
            insert.setString(4, email);
            insert.setString(5, hashedPassword);
            insert.executeUpdate();

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

        try (Connection connection = connect(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pass);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();  // If there's a match, resultSet will have at least one row.

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void displayAllData(JTextArea textArea) {
        String query = "SELECT * FROM account";

        try (Connection con = connect(); Statement statement = con.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            StringBuilder result = new StringBuilder(textArea.getText()); // Get existing text

            result.append("SAVED PASSWORDS\n");

            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String value = resultSet.getString(columnName);
                    result.append(columnName).append(": ").append(value).append("\n");
                }
                result.append("\n");
            }

            textArea.setText(result.toString());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            closeConnection();
        }
    }

    public void saveAccount(String appOrSiteName, String emailOrUsername, String password) {
        String sql = "INSERT INTO account (appOrSiteName, emailOrUsername, password) VALUES(?,?,?)";
        try {
            Connection con = connect();
            PreparedStatement insert = con.prepareStatement(sql);
            insert.setString(1, appOrSiteName);
            insert.setString(2, emailOrUsername);
            insert.setString(3, password);
            insert.executeUpdate();

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
}
