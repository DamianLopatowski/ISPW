package org.example.boundary;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.example.bean.ProdottoBean;
import org.example.view.NegozioView1;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class
NegozioView1Adapter implements NegozioBoundary {

    private final NegozioView1 view;

    public NegozioView1Adapter() {
        this.view = new NegozioView1();
    }

    public NegozioView1 getInnerView() {
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
        FlowPane contenitore = view.getFlowPaneProdotti();
        contenitore.getChildren().clear();

        for (ProdottoBean p : prodotti) {
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

            int max = Math.max(0, p.getQuantita());
            Spinner<Integer> spinner;
            if (max > 0) {
                spinner = new Spinner<>(1, max, 1);
            } else {
                spinner = new Spinner<>(0, 0, 0);
                spinner.setDisable(true);
            }

            Button aggiungi = new Button("Aggiungi");
            aggiungi.setDisable(max <= 0);

            aggiungi.setOnAction(e -> {
                int qty = spinner.getValue();
                onAddToCart.accept(p, qty);
            });

            boxProdotto.getChildren().addAll(imgView, nome, prezzo, disponibilita, spinner, aggiungi);
            contenitore.getChildren().add(boxProdotto);
        }
    }
    @Override
    public void renderCarrello(Map<ProdottoBean, Integer> carrello,
                               Consumer<ProdottoBean> onPlus,
                               Consumer<ProdottoBean> onMinus,
                               Consumer<ProdottoBean> onRemove) {

        VBox righeBox = view.getRigheCarrelloBox();
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

            VBox infoRiga = new VBox(2, nome, prezzo, qta, subtot);
            infoRiga.setPadding(new Insets(5, 5, 2, 5));
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
            bottoniRiga.setPadding(new Insets(0, 5, 10, 5));
            bottoniRiga.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

            VBox bloccoProdotto = new VBox(2, infoRiga, bottoniRiga);
            bloccoProdotto.setStyle("-fx-border-color: #ddd; -fx-border-width: 0 0 1 0;");
            bloccoProdotto.setPadding(new Insets(3, 0, 3, 0));

            righeBox.getChildren().add(bloccoProdotto);
        }
    }

    @Override
    public void setTotale(double totale) {
        view.getTotaleLabel().setText("Totale: €" + String.format("%.2f", totale));
    }

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
