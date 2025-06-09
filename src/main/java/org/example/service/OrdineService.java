package org.example.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.controllerapplicativo.SessionController;
import org.example.dao.OrdineDAOImpl;
import org.example.dao.ProdottoDAO;
import org.example.dao.ProdottoDAOImpl;
import org.example.model.Cliente;
import org.example.model.Ordine;
import org.example.model.Prodotto;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
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

        // 🔍 Recupera carrello e modalità
        Map<Prodotto, Integer> carrello = new HashMap<>(SessionController.getCarrello());
        boolean isOnline = SessionController.getIsOnlineModeStatic();

        if (carrello.isEmpty()) {
            LOGGER.warning("⚠️ Carrello vuoto, nessun ordine da processare.");
            return;
        }

        // 💰 Calcola il totale dell'ordine
        double totale = 0.0;
        for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
            Prodotto prodotto = entry.getKey();
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

        // 📦 Crea e salva l'ordine
        Ordine ordine = new Ordine(cliente, carrello, totale);
        new OrdineDAOImpl(isOnline).salvaOrdine(ordine);

        // 📉 Riduci le quantità dei prodotti
        ProdottoDAO prodottoDAO = new ProdottoDAOImpl(isOnline);
        for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
            prodottoDAO.riduciQuantita(entry.getKey().getId(), entry.getValue());
        }

        // 📧 Invia email riepilogo
        LOGGER.info("📧 Invio email riepilogo a " + cliente.getEmail() + "...");
        EmailService.sendOrderSummaryEmail(
                cliente.getEmail(),
                cliente.getNome() + " " + cliente.getCognome(),
                ordine.getProdotti()
        );

        // 🧹 Svuota il carrello
        SessionController.svuotaCarrello();

        // ✅ Notifica di conferma
        LOGGER.info("✅ Ordine confermato per il cliente: " + cliente.getUsername());
        new Alert(Alert.AlertType.INFORMATION, "✅ Ordine inviato correttamente!", ButtonType.OK).showAndWait();
    }
}
