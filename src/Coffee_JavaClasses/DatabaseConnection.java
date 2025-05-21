/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Coffee_JavaClasses;

import Coffee_1Shop.Ordering_page;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.mindrot.jbcrypt.BCrypt;
import javax.swing.*;
import java.sql.*;
/**
 *
 * @author ADMIN
 */
public class DatabaseConnection {
    
    public static boolean insertOrder(String orderName, String coffee, int quantity, double price) {
    String sql = "INSERT INTO `orders` (order_name, coffee, quantity, price) VALUES (?, ?, ?, ?)";

    try (Connection conn = getConnection(); PreparedStatement pst = conn.prepareStatement(sql)) {
        pst.setString(1, orderName);
        pst.setString(2, coffee);
        pst.setInt(3, quantity);
        pst.setDouble(4, price);

        int rowsAffected = pst.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        System.out.println("Error inserting orders: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
    

    
    public static boolean registerUser(String firstname, String lastname, String email, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        
        if (!isValidEmail(email)) {
            return false;
        }
        
        if (emailExists(email)) {
            return false;
        }
        
             
        try (Connection conn = getConnection()) {
            String query = "INSERT INTO users (firstname, lastname, email, password) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);       
            pst.setString(1, firstname);
            pst.setString(2, lastname);
            pst.setString(3, email);
            pst.setString(4, hashedPassword);  
            int rowsAffected = pst.executeUpdate();
             
            return rowsAffected > 0;
        } catch (SQLException e) {
        
            System.out.println("Error during registration: " + e.getMessage());
            e.printStackTrace();       
            return false;
        }
}
private static boolean emailExists(String email) {
    try (Connection conn = getConnection()) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, email);
        
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;  
        }
    } catch (SQLException e) {
        System.out.println("Error during email check: " + e.getMessage());
        e.printStackTrace();
    }
    return false;
}    
    
public static boolean isValidEmail(String email) {
    String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    return email.matches(regex);
}
    public static boolean checkCredentials(String email, String password) {
        try (Connection conn = getConnection()) {
            String query = "SELECT password FROM users WHERE email = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, email);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                return BCrypt.checkpw(password, storedPassword); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static boolean checkPassword(String enteredPassword, String storedPassword) {
        return BCrypt.checkpw(enteredPassword, storedPassword);
    }
    

    
    public static void rehashPasswords() {
    try (Connection conn = getConnection()) {
        String query = "SELECT email, password FROM users";
        PreparedStatement pst = conn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String email = rs.getString("email");
            String oldPassword = rs.getString("password");

            if (BCrypt.checkpw("dummy_password", oldPassword)) {
                String newHashedPassword = BCrypt.hashpw(oldPassword, BCrypt.gensalt());
                
                updatePassword(email, newHashedPassword); 
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    public static boolean updatePassword(String email, String newPassword) {
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());   
        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "UPDATE users SET password = ? WHERE email = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, hashedPassword);
            pst.setString(2, email);
            
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0; 
        } catch (SQLException e) {
            System.out.println("Error during password update: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    

    
    public static Connection getConnection() {
        try {              
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/coffeeshop";
            String username = "root";
            String password = "Harry";
            
            Connection conn = DriverManager.getConnection(url, username, password);
            if (conn != null) {
            System.out.println("Database connected successfully.");
        }
            
            return conn;
            
         
            
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Failed to connect to the database.");
            e.printStackTrace();
            return null;
            
        }
    }
}  
