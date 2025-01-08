package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection connectToDatabase() {
        try {
            // Connessione al database
            String url = "jdbc:mysql://localhost:3306/bikegarage";
            String username = "root";
            String password = "Kazik+10";
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Errore di connessione: " + e.getMessage());
            return null;
        }
    }
}
