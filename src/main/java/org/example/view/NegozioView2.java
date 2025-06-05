package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class NegozioView2 {
    private final BorderPane root;
    private final ListView<String> listaProdotti;
    private final VBox dettagliBox;
    private final ImageView imageView;
    private final Label disponibilitaLabel;
    private final TextField quantitàField;
    private final Button aggiungiButton;
    private final VBox carrelloBox;

    public NegozioView2() {
        root = new BorderPane();

        // ✅ Lista prodotti
        listaProdotti = new ListView<>();
        listaProdotti.setPrefWidth(200);

        // ✅ Box dettagli
        dettagliBox = new VBox(10);
        dettagliBox.setPadding(new Insets(10));
        imageView = new ImageView();
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        disponibilitaLabel = new Label("Disponibilità: ");
        quantitàField = new TextField();
        quantitàField.setPromptText("Quantità");
        aggiungiButton = new Button("Aggiungi al carrello");

        dettagliBox.getChildren().addAll(imageView, disponibilitaLabel, quantitàField, aggiungiButton);

        // ✅ Box carrello
        carrelloBox = new VBox(10);
        carrelloBox.setPadding(new Insets(10));
        carrelloBox.setPrefWidth(250);
        carrelloBox.setStyle("-fx-background-color: #f0f0f0;");
        carrelloBox.getChildren().add(new Label("🛒 Carrello"));

        // ✅ Layout
        root.setLeft(listaProdotti);
        root.setCenter(dettagliBox);
        root.setRight(carrelloBox);
    }

    public BorderPane getRoot() { return root; }
    public ListView<String> getListaProdotti() { return listaProdotti; }
    public VBox getDettagliBox() { return dettagliBox; }
    public ImageView getImageView() { return imageView; }
    public Label getDisponibilitaLabel() { return disponibilitaLabel; }
    public TextField getQuantitàField() { return quantitàField; }
    public Button getAggiungiButton() { return aggiungiButton; }
    public VBox getCarrelloBox() { return carrelloBox; }
}
