package org.example.controllergrafici;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.example.dao.ProdottoDAOImpl;
import org.example.model.Prodotto;
import org.example.view.NegozioView;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.logging.Logger;

public class NegozioController {
    private static final Logger logger = Logger.getLogger(NegozioController.class.getName());
    private final NegozioView view;
    private final ProdottoDAOImpl prodottoDAO;
    private final Map<Prodotto, Integer> carrello = new HashMap<>();

    public NegozioController(boolean isOnlineMode) {
        view = new NegozioView();
        prodottoDAO = new ProdottoDAOImpl(isOnlineMode);

        aggiornaListaProdotti();

        Button inviaOrdine = new Button("Invia Ordine");
        inviaOrdine.setOnAction(e -> {
            for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
                prodottoDAO.riduciQuantita(entry.getKey().getId(), entry.getValue());
                logger.info("🛒 Ordinato: " + entry.getKey().getNome() + " x" + entry.getValue());
            }
            carrello.clear();
            aggiornaCarrello();
            aggiornaListaProdotti();
        });

        view.getCarrelloBox().getChildren().add(inviaOrdine);
    }

    private void aggiornaListaProdotti() {
        FlowPane contenitore = view.getFlowPaneProdotti();
        contenitore.getChildren().clear();

        for (Prodotto p : prodottoDAO.getAllProdotti()) {
            VBox boxProdotto = new VBox(5);
            boxProdotto.setPadding(new Insets(10));
            boxProdotto.setStyle("-fx-border-color: lightgray; -fx-background-color: white;");
            boxProdotto.setPrefWidth(200); // larghezza fissa, ma può adattarsi

            ImageView imgView = new ImageView();
            if (p.getImmagine() != null) {
                Image img = new Image(new ByteArrayInputStream(p.getImmagine()));
                imgView.setImage(img);
                imgView.setFitHeight(80);
                imgView.setPreserveRatio(true);
            }

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

            boxProdotto.getChildren().addAll(imgView, nome, prezzo, disponibilita, spinner, aggiungi);
            contenitore.getChildren().add(boxProdotto);
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
