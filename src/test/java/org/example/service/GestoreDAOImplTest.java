package org.example.service;
import org.example.dao.GestoreDAOImpl;
import org.example.model.Gestore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GestoreDAOImplTest {

    private GestoreDAOImpl gestoreDAO;

    @BeforeEach
    void setUp() {
        gestoreDAO = new GestoreDAOImpl(); // Usa config.properties già presente
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
        Gestore risultato = gestoreDAO.findByUsername("qualcuno");
        assertNull(risultato);
    }

    @Test
    void testAuthenticateOnlineRestituisceFalseSenzaDatabase() {
        boolean risultato = gestoreDAO.authenticateOnline("adminOffline", "offline123");
        assertFalse(risultato);
    }
}
