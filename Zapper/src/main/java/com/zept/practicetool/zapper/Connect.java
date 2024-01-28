package com.zept.practicetool.zapper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Allen James Laxamana
 */
public class Connect {
    // Get the present working directory
    static String relativePath = System.getProperty("user.dir") + File.separator + "db" + File.separator + "?.db";
    // Connection string
    static String url = "jdbc:sqlite:" + relativePath;
    static Connection con = null;
    // Connect to the database
    public static Connection connect() {
        try {
            con = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return con;
    }
    
    // Terminate database connection
    public static void closeConnection() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    // Create a new table if there is none
    public static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS user (\n"
            + "email text PRIMARY KEY,\n" 
            + "password text NOT NULL,\n"
            + "firstName text,\n"
            + "middleName text,\n"
            + "lastName text,\n"
            + ");";
        try {
            con = DriverManager.getConnection(url);
            Statement insert = con.createStatement();
            insert.execute(sql);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}




