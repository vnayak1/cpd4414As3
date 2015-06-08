/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseCrediential;


import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author vinayak
 */

public class Credentials {

    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String jdbc = "jdbc:mysql://localhost/c0651659";
            String user = "root";
            String pass = "";
            conn = DriverManager.getConnection(jdbc, user, pass);
           

        } catch (ClassNotFoundException ex) {
            System.err.println("No class found Exception" + ex.getMessage());
        }
        return conn;
    }
}