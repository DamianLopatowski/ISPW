package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {

    public static boolean verifyCredentials(String username, String password) {
        // Prepara la query SQL per verificare le credenziali
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Imposta i parametri della query
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Esegui la query e ottieni il risultato
            ResultSet rs = stmt.executeQuery();

            // Se c'è almeno un risultato, le credenziali sono corrette
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Errore durante la verifica delle credenziali: " + e.getMessage());
            return false;
        }
    }
}
