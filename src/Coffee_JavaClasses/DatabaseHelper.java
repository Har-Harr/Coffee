/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Coffee_JavaClasses;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
/**
 *
 * @author ADMIN
 */

public class DatabaseHelper {  
  
    public static boolean isEmailRegistered(String email) { 
        boolean exists = false;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coffeeshop", "root", "Harry");
            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            exists = rs.next();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exists;
    }
    public static boolean updatePassword(String email, String newPassword) {
        Connection connection = null;
        PreparedStatement stmt = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/coffeeshop", "root", "Harry");
            
            String sql = "UPDATE users SET password = ? WHERE email = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, newPassword);
            stmt.setString(2, email);
            
            int rowsAffected = stmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
         } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void storeVerificationCode(String email, String code) {
        Connection conn = null;
        PreparedStatement stmt = null;
  
        try {
            System.out.println("Storing verification code for email: " + email + " with code: " + code);
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coffeeshop", "root", "Harry");
            String sql = "UPDATE users SET verification_code = ? WHERE email = ?";
            stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, code);
            stmt.setString(2, email);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Verification code stored successfully.");
            } else {
                System.out.println("No user found with the provided email, or verification code was not updated.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred while storing verification code: " + e.getMessage());
            e.printStackTrace();
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
        public static String getStoredPassword(String email) {
    try (Connection conn = getConnection()) {
        String query = "SELECT password FROM users WHERE email = ?";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, email);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return rs.getString("password");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    public static String getVerificationCode(String email) {
        String code = null;
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/coffeeshop", "root", "Harry");
            String query = "SELECT verification_code FROM users WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);
            
            System.out.println("Executing query: " + query);
            ResultSet rs = stmt.executeQuery();
            
            
            if (rs.next()) {
                code = rs.getString("verification_code");
                System.out.println("Fetched Code: " + code);
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
        private static final String DB_URL = "jdbc:mysql://localhost:3306/coffeeshop";
    private static final String USER = "root"; 
    private static final String PASS = "Harry"; 
        
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("Database driver not found.");
        }
    }
            
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void getUsers() {
        String sql = "SELECT * FROM users";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("User: " + rs.getString("firstname"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
    }
}
