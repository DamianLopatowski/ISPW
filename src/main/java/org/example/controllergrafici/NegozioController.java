package org.example.controllergrafici;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.example.dao.ProdottoDAOImpl;
import org.example.model.Prodotto;
import org.example.view.NegozioView1;
import org.example.view.NegozioView2;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.logging.Logger;

public class NegozioController {
    private static final Logger logger = Logger.getLogger(NegozioController.class.getName());
    private final Object view;
    private final ProdottoDAOImpl prodottoDAO;
    private final Map<Prodotto, Integer> carrello = new HashMap<>();

    public NegozioController(boolean isOnlineMode, boolean isInterfaccia1) {
        this.view = isInterfaccia1 ? new NegozioView1() : new NegozioView2();
        this.prodottoDAO = new ProdottoDAOImpl(isOnlineMode);

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

        getCarrelloBox().getChildren().add(inviaOrdine);
    }

    private void aggiornaListaProdotti() {
        if (view instanceof NegozioView1) {
            NegozioView1 v1 = (NegozioView1) view;
            FlowPane contenitore = v1.getFlowPaneProdotti();
            contenitore.getChildren().clear();

            for (Prodotto p : prodottoDAO.getAllProdotti()) {
                VBox boxProdotto = new VBox(5);
                boxProdotto.setPadding(new Insets(10));
                boxProdotto.setStyle("-fx-border-color: lightgray; -fx-background-color: white;");
                boxProdotto.setPrefWidth(200);

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

        } else if (view instanceof NegozioView2) {
            NegozioView2 v2 = (NegozioView2) view;
            ListView<String> lista = v2.getListaProdotti();
            lista.getItems().clear();
            Map<String, Prodotto> prodottiMap = new HashMap<>();

            for (Prodotto p : prodottoDAO.getAllProdotti()) {
                String nomeVisualizzato = p.getNome() + " - €" + p.getPrezzoVendita();
                prodottiMap.put(nomeVisualizzato, p);
                lista.getItems().add(nomeVisualizzato);
            }

            // seleziona automaticamente il primo prodotto
            if (!lista.getItems().isEmpty()) {
                lista.getSelectionModel().selectFirst();
                aggiornaDettagliProdotto(lista.getSelectionModel().getSelectedItem(), prodottiMap, v2);
            }

            lista.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                aggiornaDettagliProdotto(newVal, prodottiMap, v2);
            });
        }
    }

    private void aggiornaDettagliProdotto(String nome, Map<String, Prodotto> prodottiMap, NegozioView2 v2) {
        Prodotto selezionato = prodottiMap.get(nome);
        if (selezionato == null) return;

        if (selezionato.getImmagine() != null) {
            Image img = new Image(new ByteArrayInputStream(selezionato.getImmagine()));
            v2.getImageView().setImage(img);
        } else {
            v2.getImageView().setImage(null);
        }

        v2.getDisponibilitaLabel().setText("Disponibili: " + selezionato.getQuantita());

        v2.getAggiungiButton().setOnAction(e -> {
            try {
                int q = Integer.parseInt(v2.getQuantitàField().getText().trim());
                if (q > 0 && q <= selezionato.getQuantita()) {
                    carrello.put(selezionato, carrello.getOrDefault(selezionato, 0) + q);
                    aggiornaCarrello();
                } else {
                    showAlert("Quantità non valida");
                }
            } catch (NumberFormatException ex) {
                showAlert("Inserisci un numero valido");
            }
        });
    }

    private void aggiornaCarrello() {
        VBox box = getCarrelloBox();
        box.getChildren().removeIf(n -> n instanceof Label && !((Label) n).getText().equals("🛒 Carrello"));
        for (Map.Entry<Prodotto, Integer> entry : carrello.entrySet()) {
            box.getChildren().add(new Label(entry.getKey().getNome() + " x" + entry.getValue()));
        }
    }

    private VBox getCarrelloBox() {
        if (view instanceof NegozioView1) return ((NegozioView1) view).getCarrelloBox();
        else if (view instanceof NegozioView2) return ((NegozioView2) view).getCarrelloBox();
        return new VBox();
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.showAndWait();
    }

    public Parent getRootView() {
        if (view instanceof NegozioView1) return ((NegozioView1) view).getRoot();
        else if (view instanceof NegozioView2) return ((NegozioView2) view).getRoot();
        return null;
    }
}
