package org.example.service;

import org.example.dao.GestoreDAOImpl;
import org.example.model.Gestore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class GestoreDAOImplTest {

    private GestoreDAOImpl gestoreDAO;

    @BeforeEach
    void setUp() throws IOException {
        Properties props = new Properties();
        props.setProperty("username", "adminOffline");
        props.setProperty("password", "offline123");
        try (FileOutputStream out = new FileOutputStream("config.pr")) {
            props.store(out, null);
        }

        gestoreDAO = new GestoreDAOImpl(); // carica config.properties
    }

    @Test
    void testOfflineGestoreCaricatoCorrettamente() {
        Gestore gestore = gestoreDAO.getGestore();
        assertNotNull(gestore);
        assertEquals("admin", gestore.getUsername());
        assertEquals("password", gestore.getPassword());
    }

    @Test
    void testResetToOfflineGestoreMantieneCredenziali() {
        Gestore originale = gestoreDAO.getGestore();
        gestoreDAO.resetToOfflineGestore();
        Gestore dopoReset = gestoreDAO.getGestore();
        assertEquals(originale.getUsername(), dopoReset.getUsername());
        assertEquals(originale.getPassword(), dopoReset.getPassword());
    }

    @Test
    void testFindByUsernameOfflineRestituisceNull() {
        // In modalità offline non dovrebbe accedere al DB
        Gestore risultato = gestoreDAO.findByUsername("qualcuno");
        assertNull(risultato);
    }

    @Test
    void testAuthenticateOnlineRestituisceFalseSenzaDatabase() {
        // Essendo offline, la connessione fallisce → dovrebbe restituire false
        boolean risultato = gestoreDAO.authenticateOnline("adminOffline", "offline123");
        assertFalse(risultato);
    }
}
