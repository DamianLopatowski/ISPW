package org.example.service;

import org.example.bean.ClienteBean;
import org.example.dao.ClienteDAO;
import org.example.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegistrazioneServiceTest {

    private ClienteDAO clienteDAOMock;
    private RegistrazioneService registrazioneService;

    @BeforeEach
    public void setUp() {
        clienteDAOMock = mock(ClienteDAO.class);
        registrazioneService = new RegistrazioneService(clienteDAOMock, "online");
    }
    @Test
    public void testRegistrazioneSuccess() throws RegistrazioneException {
        ClienteBean clienteBean = new ClienteBean();
        clienteBean.setUsername("utente123");
        clienteBean.setNome("Mario");
        clienteBean.setCognome("Rossi");
        clienteBean.setPassword("Password1!");
        clienteBean.setEmail("mario@example.com");
        clienteBean.setIndirizzo("Via Roma 1");
        clienteBean.setPartitaIva("12345678901");
        clienteBean.setCivico("1");
        clienteBean.setCap("00100");

        when(clienteDAOMock.findByUsername("utente123")).thenReturn(null);
        when(clienteDAOMock.findByEmail("mario@example.com")).thenReturn(null);

        assertDoesNotThrow(() -> registrazioneService.registraCliente(clienteBean));
        verify(clienteDAOMock, times(1)).saveCliente(any(Cliente.class));
    }

    @Test
    public void testRegistrazioneUsernameGiaEsistente() throws RegistrazioneException {
        ClienteBean clienteBean = new ClienteBean();
        clienteBean.setUsername("utente123");
        clienteBean.setNome("Mario");
        clienteBean.setCognome("Rossi");
        clienteBean.setPassword("Password1!");
        clienteBean.setEmail("mario@example.com");
        clienteBean.setIndirizzo("Via Roma 1");
        clienteBean.setPartitaIva("12345678901");
        clienteBean.setCivico("1");
        clienteBean.setCap("00100");

        when(clienteDAOMock.findByUsername("utente123"))
                .thenReturn(new Cliente.Builder().username("utente123").build());

        RegistrazioneException ex = assertThrows(RegistrazioneException.class,
                () -> registrazioneService.registraCliente(clienteBean));

        assertEquals("Lo username è già in uso. Scegli un altro username.", ex.getMessage());
        verify(clienteDAOMock, never()).saveCliente(any(Cliente.class));
    }
}
