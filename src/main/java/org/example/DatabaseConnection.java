package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {

    // Logger per gestire i messaggi di log
    private static final Logger logger = Logger.getLogger(DatabaseConnection.class.getName());

    // Costruttore privato per impedire l'istanza della classe
    private DatabaseConnection() {
        throw new UnsupportedOperationException("La classe utility non deve essere istanziata");
    }

    // Metodo statico per connettersi al database
    public static Connection connectToDatabase() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException | IOException e) {
            // Utilizzo del logger per registrare l'errore
            logger.log(Level.SEVERE, "Errore di connessione: " + e.getMessage(), e);
            return null;
        }
    }
}
