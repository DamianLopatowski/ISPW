package org.example.service;

import org.example.bean.ProdottoBean;
import org.example.controllerapplicativo.SessionController;
import org.example.model.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class OrdineServiceTest {

    private OrdineService ordineService;
    private NavigationService navigationService;

    @BeforeEach
    public void setUp() {
        navigationService = mock(NavigationService.class);

        // Creazione OrdineService con override di mostraConfermaOrdine
        ordineService = new OrdineService(navigationService) {
            protected void mostraConfermaOrdine() {
                // Override vuoto per evitare Alert JavaFX nei test
            }
        };
    }
    @Test
    public void testProcediOrdine_conCarrelloValido() {
        Cliente clienteMock = mock(Cliente.class);
        when(clienteMock.getUsername()).thenReturn("testUser");
        when(clienteMock.getEmail()).thenReturn("test@example.com");
        when(navigationService.getClienteLoggato()).thenReturn(clienteMock);

        ProdottoBean prodotto = new ProdottoBean();
        prodotto.setId(1);
        prodotto.setNome("TestProdotto");
        prodotto.setPrezzoVendita(10.0);

        Map<ProdottoBean, Integer> carrello = new HashMap<>();
        carrello.put(prodotto, 2);

        SessionController.setCarrello(carrello);
        SessionController.setIsOnlineModeStatic(false);

        ordineService.procediOrdine();

        // Se non ci sono eccezioni, il test è considerato superato
    }

    @Test
    public void testProcediOrdine_clienteNullo() {
        when(navigationService.getClienteLoggato()).thenReturn(null);
        ordineService.procediOrdine();
        // Non deve lanciare eccezione
    }

    @Test
    public void testProcediOrdine_carrelloVuoto() {
        Cliente clienteMock = mock(Cliente.class);
        when(clienteMock.getUsername()).thenReturn("user");
        when(navigationService.getClienteLoggato()).thenReturn(clienteMock);

        SessionController.setCarrello(new HashMap<>());
        SessionController.setIsOnlineModeStatic(true);

        ordineService.procediOrdine();
        // Non deve lanciare eccezione
    }
}
