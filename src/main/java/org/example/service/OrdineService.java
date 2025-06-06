// OrdineService.java
package org.example.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.dao.OrdineDAOImpl;
import org.example.model.Cliente;
import org.example.model.Ordine;
import org.example.model.Prodotto;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class OrdineService {

    private static final Logger LOGGER = Logger.getLogger(OrdineService.class.getName());
    private final NavigationService navigationService;

    public OrdineService(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    public void procediOrdine() {
        Cliente cliente = navigationService.getClienteLoggato();
        if (cliente == null) {
            LOGGER.severe("❌ Errore: cliente nullo in OrdineService!");
            return;
        }

        LOGGER.info("Ordine confermato per il cliente: " + cliente.getUsername());
        new Alert(Alert.AlertType.INFORMATION, "✅ Ordine inviato correttamente!", ButtonType.OK).showAndWait();
    }

    public void salvaOrdineOnline(Map<Prodotto, Integer> carrello, boolean isOnline) {
        Cliente cliente = navigationService.getClienteLoggato();
        if (cliente == null) {
            LOGGER.warning("⚠️ Nessun cliente loggato.");
            return;
        }

        LOGGER.info("📦 Dettaglio ordine per: " + cliente.getNome() + " " + cliente.getCognome());
        double totale = 0.0;

        for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
            Prodotto prodotto = entry.getKey();
            int q = entry.getValue();
            double subtotale = prodotto.getPrezzoVendita() * q;
            totale += subtotale;

            LOGGER.info(String.format("- %s x%d → €%.2f", prodotto.getNome(), q, subtotale));
        }

        LOGGER.info(String.format("Totale: €%.2f", totale));

        Ordine ordine = new Ordine(cliente, new HashMap<>(carrello), totale);

        new OrdineDAOImpl(isOnline).salvaOrdine(ordine);
    }
}
