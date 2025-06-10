package org.example.service;

import org.example.bean.ProdottoBean;
import org.example.controllerapplicativo.SessionController;
import org.example.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class OrdineServiceTest {

    private OrdineService ordineService;
    private NavigationService navigationService;

    @BeforeEach
    public void setUp() {
        // Mock del NavigationService
        navigationService = mock(NavigationService.class);
        ordineService = new OrdineService(navigationService);
    }

    @Test
    public void testProcediOrdine_conCarrelloValido() {
        // Mock cliente
        Cliente clienteMock = mock(Cliente.class);
        when(clienteMock.getUsername()).thenReturn("testUser");
        when(clienteMock.getEmail()).thenReturn("test@example.com");
        when(navigationService.getClienteLoggato()).thenReturn(clienteMock);

        // Mock carrello
        ProdottoBean prodotto = new ProdottoBean();
        prodotto.setId(1);
        prodotto.setNome("TestProdotto");
        prodotto.setPrezzoVendita(10.0);

        Map<ProdottoBean, Integer> carrello = new HashMap<>();
        carrello.put(prodotto, 2);

        // Simuliamo carrello e modalità statica
        SessionController.setCarrello(carrello);
        SessionController.setIsOnlineModeStatic(false); // test in modalità offline

        // Chiamata al metodo
        ordineService.procediOrdine();

        // Se non ci sono eccezioni, il test passa (verifica manuale via log)
    }

    @Test
    public void testProcediOrdine_clienteNullo() {
        when(navigationService.getClienteLoggato()).thenReturn(null);
        ordineService.procediOrdine();
        // Dovrebbe loggare errore, ma non lanciare eccezioni
    }

    @Test
    public void testProcediOrdine_carrelloVuoto() {
        Cliente clienteMock = mock(Cliente.class);
        when(clienteMock.getUsername()).thenReturn("user");
        when(navigationService.getClienteLoggato()).thenReturn(clienteMock);

        SessionController.setCarrello(new HashMap<>()); // carrello vuoto
        SessionController.setIsOnlineModeStatic(true);

        ordineService.procediOrdine();
        // Dovrebbe loggare warning
    }
}
