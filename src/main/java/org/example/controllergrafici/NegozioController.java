package org.example.controllergrafici;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.dao.ProdottoDAOImpl;
import org.example.model.Prodotto;
import org.example.view.NegozioView;

import java.io.ByteArrayInputStream;
import java.util.*;

public class NegozioController {
    private final NegozioView view;
    private final ProdottoDAOImpl prodottoDAO;
    private final Map<Prodotto, Integer> carrello = new HashMap<>();

    public NegozioController(Stage stage, boolean isOnlineMode) {
        view = new NegozioView();
        prodottoDAO = new ProdottoDAOImpl(isOnlineMode);

        aggiornaListaProdotti();

        Button inviaOrdine = new Button("Invia Ordine");
        inviaOrdine.setOnAction(e -> {
            for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
                prodottoDAO.riduciQuantita(entry.getKey().getId(), entry.getValue());
                System.out.println("🛒 Ordinato: " + entry.getKey().getNome() + " x" + entry.getValue());
            }
            carrello.clear();
            aggiornaCarrello();
            aggiornaListaProdotti();
        });

        view.getCarrelloBox().getChildren().add(inviaOrdine);
    }

    private void aggiornaListaProdotti() {
        VBox lista = view.getListaProdotti();
        lista.getChildren().clear();

        for (Prodotto p : prodottoDAO.getAllProdotti()) {
            HBox riga = new HBox(10);

            // Immagine
            ImageView imgView = new ImageView();
            if (p.getImmagine() != null) {
                Image img = new Image(new ByteArrayInputStream(p.getImmagine()));
                imgView.setImage(img);
                imgView.setFitHeight(50);
                imgView.setPreserveRatio(true);
            }

            VBox infoBox = new VBox(5);
            Label nome = new Label(p.getNome());
            Label prezzo = new Label("€" + p.getPrezzoVendita());
            Label disponibilita = new Label("Disponibili: " + p.getQuantita());

            Spinner<Integer> spinner = new Spinner<>(1, p.getQuantita(), 1);
            Button aggiungi = new Button("Aggiungi");

            aggiungi.setOnAction(e -> {
                int quantita = spinner.getValue();
                carrello.put(p, carrello.getOrDefault(p, 0) + quantita);
                aggiornaCarrello();
            });

            infoBox.getChildren().addAll(nome, prezzo, disponibilita, spinner, aggiungi);
            riga.getChildren().addAll(imgView, infoBox);
            lista.getChildren().add(riga);
        }
    }

    private void aggiornaCarrello() {
        VBox box = view.getCarrelloBox();
        box.getChildren().removeIf(n -> n instanceof Label && !((Label) n).getText().equals("🛒 Carrello"));
        for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
            box.getChildren().add(new Label(entry.getKey().getNome() + " x" + entry.getValue()));
        }
    }

    public Parent getRootView() {
        return view.getRoot();
    }
}
