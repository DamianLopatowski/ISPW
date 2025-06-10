package org.example.dao;

import org.example.model.Gestore;

public interface GestoreDAO {
    Gestore findByUsername(String username);
}
