package org.example.boundary;

import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import org.example.bean.ProdottoBean;
import org.example.view.NegozioView2;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class NegozioView2Adapter implements NegozioBoundary {

    private final NegozioView2 view;

    private final Map<String, ProdottoBean> prodottiMap = new HashMap<>();

    public NegozioView2Adapter() {
        this.view = new NegozioView2();
    }

    public NegozioView2 getInnerView() {
        return view;
    }

    @Override
    public Parent getRoot() {
        return view.getRoot();
    }

    @Override
    public void setOnInviaOrdine(Runnable action) {
        view.getInviaOrdineButton().setOnAction(e -> action.run());
    }

    @Override
    public void setOnLogout(Runnable action) {
        view.getLogoutButton().setOnAction(e -> action.run());
    }

    @Override
    public void setOnProfilo(Runnable action) {
        view.getProfiloButton().setOnAction(e -> action.run());
    }

    @Override
    public void setOnVisualizzaPagamenti(Runnable action) {
        view.getVisualizzaPagamentiButton().setOnAction(e -> action.run());
    }

    @Override
    public void renderProdotti(List<ProdottoBean> prodotti, BiConsumer<ProdottoBean, Integer> onAddToCart) {
        ListView<String> lista = view.getListaProdotti();
        lista.getItems().clear();
        prodottiMap.clear();

        for (ProdottoBean p : prodotti) {
            String nomeVisualizzato = p.getNome() + " - €" + p.getPrezzoVendita();
            prodottiMap.put(nomeVisualizzato, p);
            lista.getItems().add(nomeVisualizzato);
        }

        if (!lista.getItems().isEmpty()) {
            lista.getSelectionModel().selectFirst();
            aggiornaDettagliProdotto(lista.getSelectionModel().getSelectedItem());
        } else {
            view.getImageView().setImage(null);
            view.getDisponibilitaLabel().setText("Disponibili: 0");
        }

        lista.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            aggiornaDettagliProdotto(newVal);
        });

        view.getAggiungiButton().setOnAction(e -> {
            String selectedKey = lista.getSelectionModel().getSelectedItem();
            ProdottoBean selezionato = prodottiMap.get(selectedKey);
            if (selezionato == null) {
                showInfo("Seleziona un prodotto.");
                return;
            }

            try {
                int q = Integer.parseInt(view.getQuantitaField().getText().trim());
                if (q > 0 && q <= selezionato.getQuantita()) {
                    onAddToCart.accept(selezionato, q);
                } else {
                    showInfo("Quantità non valida");
                }
            } catch (NumberFormatException ex) {
                showInfo("Inserisci un numero valido");
            }
        });
    }

    private void aggiornaDettagliProdotto(String key) {
        ProdottoBean selezionato = prodottiMap.get(key);
        if (selezionato == null) return;

        if (selezionato.getImmagine() != null) {
            Image img = new Image(new ByteArrayInputStream(selezionato.getImmagine()));
            view.getImageView().setImage(img);
        } else {
            view.getImageView().setImage(null);
        }

        view.getDisponibilitaLabel().setText("Disponibili: " + selezionato.getQuantita());
    }

    @Override
    public void renderCarrello(Map<ProdottoBean, Integer> carrello,
                               Consumer<ProdottoBean> onPlus,
                               Consumer<ProdottoBean> onMinus,
                               Consumer<ProdottoBean> onRemove) {

        javafx.scene.layout.VBox righeBox = view.getRigheCarrelloBox();
        righeBox.getChildren().clear();

        for (Map.Entry<ProdottoBean, Integer> entry : carrello.entrySet()) {
            ProdottoBean prodotto = entry.getKey();
            int quantita = entry.getValue();

            double prezzoUnitario = prodotto.getPrezzoVendita();
            double subtotale = prezzoUnitario * quantita;

            Label nome = new Label(prodotto.getNome());
            nome.setWrapText(true);
            nome.setMaxWidth(130);
            nome.setStyle("-fx-font-weight: bold;");

            Label prezzo = new Label("Prezzo: €" + String.format("%.2f", prezzoUnitario));
            Label qta = new Label("Quantità: x" + quantita);
            Label subtot = new Label("Subtotale: €" + String.format("%.2f", subtotale));

            javafx.scene.layout.VBox infoRiga = new javafx.scene.layout.VBox(2, nome, prezzo, qta, subtot);
            infoRiga.setPadding(new javafx.geometry.Insets(5, 5, 2, 5));
            infoRiga.setStyle("-fx-alignment: CENTER_LEFT;");

            Button plus = new Button("+");
            Button minus = new Button("-");
            Button remove = new Button("x");

            plus.setOnAction(e -> onPlus.accept(prodotto));
            minus.setOnAction(e -> onMinus.accept(prodotto));
            remove.setOnAction(e -> onRemove.accept(prodotto));

            javafx.scene.layout.Region spacer = new javafx.scene.layout.Region();
            javafx.scene.layout.HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            javafx.scene.layout.HBox bottoniRiga = new javafx.scene.layout.HBox(10, spacer, plus, minus, remove);
            bottoniRiga.setPadding(new javafx.geometry.Insets(0, 5, 10, 5));
            bottoniRiga.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

            javafx.scene.layout.VBox bloccoProdotto = new javafx.scene.layout.VBox(2, infoRiga, bottoniRiga);
            bloccoProdotto.setStyle("-fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");
            bloccoProdotto.setPadding(new javafx.geometry.Insets(3, 0, 3, 0));

            righeBox.getChildren().add(bloccoProdotto);
        }
    }

    @Override
    public void setTotale(double totale) {
        view.getTotaleLabel().setText("Totale: €" + String.format("%.2f", totale));
    }

    // -------------------------
    // UI helpers
    // -------------------------

    @Override
    public void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }

    @Override
    public Optional<ConfermaChoice> showConfermaOrdine(String riepilogo) {
        ButtonType annulla = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType modifica = new ButtonType("Modifica Spedizione");
        ButtonType conferma = new ButtonType("Conferma Ordine");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, riepilogo, annulla, modifica, conferma);
        alert.setTitle("Conferma Ordine");
        alert.setHeaderText("Vuoi procedere con l’ordine?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty()) return Optional.empty();

        if (result.get() == annulla) return Optional.of(ConfermaChoice.ANNULLA);
        if (result.get() == modifica) return Optional.of(ConfermaChoice.MODIFICA_SPEDIZIONE);
        if (result.get() == conferma) return Optional.of(ConfermaChoice.CONFERMA);

        return Optional.empty();
    }
}
