package org.example.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseUtils {

    private static final Logger LOGGER = Logger.getLogger(DatabaseUtils.class.getName());

    // Costruttore privato per evitare l'istanza della classe
    private DatabaseUtils() {
        throw new UnsupportedOperationException("Non è possibile creare istanze di questa classe");
    }

    public static boolean verifyCredentials(String username, String password) {
        // Prepara la query SQL per verificare le credenziali (selezioniamo solo i campi necessari)
        String query = "SELECT username FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.connectToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Imposta i parametri della query
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Esegui la query e ottieni il risultato
            ResultSet rs = stmt.executeQuery();

            // Restituisce true se c'è almeno un risultato
            return rs.next();
        } catch (SQLException e) {
            LOGGER.severe("Errore durante la verifica delle credenziali: " + e.getMessage());
            return false;
        }
    }
}