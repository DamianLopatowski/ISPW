package org.example.dao;

import org.example.model.Cliente;

public interface ClienteDAO {
    void saveCliente(Cliente cliente);
    Cliente findByUsername(String username);
}