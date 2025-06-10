package org.example.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.bean.ProdottoBean;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.*;
import org.example.facade.ClienteFacade;
import org.example.model.Cliente;
import org.example.model.Ordine;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrdineService {

    private static final Logger LOGGER = Logger.getLogger(OrdineService.class.getName());
    private final NavigationService navigationService;
    private final Consumer<String> notifica;

    // Costruttore di default: usa Alert per mostrare conferma
    public OrdineService(NavigationService navigationService) {
        this(navigationService, msg -> new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait());
    }

    // Costruttore con Consumer custom (utile per i test)
    public OrdineService(NavigationService navigationService, Consumer<String> notifica) {
        this.navigationService = navigationService;
        this.notifica = notifica;
    }

    public void procediOrdine() {
        Cliente cliente = navigationService.getClienteLoggato();
        if (cliente == null) {
            LOGGER.severe("Errore: cliente nullo in OrdineService!");
            return;
        }

        Map<ProdottoBean, Integer> carrello = new HashMap<>(SessionController.getCarrello());
        boolean isOnline = SessionController.getIsOnlineModeStatic();

        if (carrello.isEmpty()) {
            LOGGER.warning("Carrello vuoto, nessun ordine da processare.");
            return;
        }

        // Calcolo del totale
        double totale = 0.0;
        for (Map.Entry<ProdottoBean, Integer> entry : carrello.entrySet()) {
            ProdottoBean prodotto = entry.getKey();
            int q = entry.getValue();
            double subtotale = prodotto.getPrezzoVendita() * q;
            totale += subtotale;

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.info(String.format("- %s x%d → €%.2f", prodotto.getNome(), q, subtotale));
            }
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(String.format("Totale: €%.2f", totale));
        }

        // Creazione e salvataggio ordine
        Ordine ordine = Ordine.creaDaBean(cliente, carrello, totale);
        new OrdineDAOImpl(isOnline).salvaOrdine(ordine);

        // Riduzione quantità disponibili
        ProdottoDAO prodottoDAO = new ProdottoDAOImpl(isOnline);
        for (Map.Entry<ProdottoBean, Integer> entry : carrello.entrySet()) {
            prodottoDAO.riduciQuantita(entry.getKey().getId(), entry.getValue());
        }

        // Invio email
        LOGGER.info("Invio email riepilogo a " + cliente.getEmail() + "...");
        PagamentoDAO pagamentoDAO = new PagamentoDAOImpl(isOnline);
        ClienteFacade facade = new ClienteFacade(pagamentoDAO);
        facade.inviaEmailRiepilogoOrdine(cliente, ordine.getProdotti());

        // Svuota carrello
        SessionController.svuotaCarrello();

        // Notifica finale
        LOGGER.info("Ordine confermato per il cliente: " + cliente.getUsername());
        notifica.accept("Ordine inviato correttamente!");
    }
}
