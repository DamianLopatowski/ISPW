package org.example.dao;

import org.example.model.Cliente;

public interface ClienteDAO {
    void saveCliente(Cliente cliente);
    Cliente findByEmail(String email);
    Cliente findByUsername(String username);
    boolean update(Cliente cliente, String vecchioUsername);

}