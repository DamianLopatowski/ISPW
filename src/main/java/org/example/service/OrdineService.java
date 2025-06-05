package org.example.service;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.example.controllerapplicativo.SessionController;
import org.example.model.Cliente;
import org.example.model.Prodotto;

import java.util.Map;

public class OrdineService {

    public void procediOrdine() {
        Cliente cliente = SessionController.getClienteLoggato();
        if (cliente == null) {
            System.err.println("❌ Errore: cliente nullo in OrdineService!");
            return;
        }

        System.out.println("Ordine confermato per il cliente: " + cliente.getUsername());
        new Alert(Alert.AlertType.INFORMATION, "✅ Ordine inviato correttamente!", ButtonType.OK).showAndWait();
    }

    public void salvaOrdineOnline(Cliente cliente, Map<Prodotto, Integer> carrello) {
        System.out.println("📦 Dettaglio ordine per: " + cliente.getNome() + " " + cliente.getCognome());
        double totale = 0.0;

        for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
            Prodotto prodotto = entry.getKey();
            int q = entry.getValue();
            double subtotale = prodotto.getPrezzoVendita() * q;
            totale += subtotale;

            System.out.println("- " + prodotto.getNome() + " x" + q + " → €" + String.format("%.2f", subtotale));
        }

        System.out.println("Totale: €" + String.format("%.2f", totale));
    }
}
