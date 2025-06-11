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
    private final TextField quantitaField;
    private final Button aggiungiButton;
    private final VBox carrelloBox;
    private final VBox righeCarrelloBox; //VBox per righe dinamiche del carrello
    private final Label totaleLabel;     //Etichetta per il totale

    private final Button inviaOrdineButton = new Button("Invia Ordine");
    private final Button logoutButton = new Button("Logout");
    private final Button profiloButton = new Button("Modifica Profilo");
    private final Button visualizzaPagamentiButton = new Button("Pagamenti & Ordini");


    public NegozioView2() {
        root = new BorderPane();

        listaProdotti = new ListView<>();
        listaProdotti.setPrefWidth(200);

        dettagliBox = new VBox(10);
        dettagliBox.setPadding(new Insets(10));
        imageView = new ImageView();
        imageView.setFitHeight(120);
        imageView.setPreserveRatio(true);

        disponibilitaLabel = new Label("Disponibilità: ");
        quantitaField = new TextField();
        quantitaField.setPromptText("Quantità");
        aggiungiButton = new Button("Aggiungi al carrello");

        dettagliBox.getChildren().addAll(imageView, disponibilitaLabel, quantitaField, aggiungiButton);

        carrelloBox = new VBox(10);
        carrelloBox.setPadding(new Insets(10));
        carrelloBox.setPrefWidth(270);
        carrelloBox.setStyle("""
            -fx-background-color: #f0f0f0;
            -fx-padding: 15;
            -fx-font-size: 13px;
            -fx-border-color: #cccccc;
            -fx-border-width: 1px;
        """);

        Label titoloCarrello = new Label("Carrello");
        titoloCarrello.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        righeCarrelloBox = new VBox(5);

        totaleLabel = new Label("Totale: €0.00");
        totaleLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        carrelloBox.getChildren().addAll(
                titoloCarrello,
                righeCarrelloBox,
                totaleLabel,
                inviaOrdineButton,
                logoutButton,
                profiloButton,
                visualizzaPagamentiButton
        );

        ScrollPane scrollCarrello = new ScrollPane(carrelloBox);
        scrollCarrello.setFitToWidth(true);
        scrollCarrello.setStyle("-fx-background-color: transparent;");
        scrollCarrello.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollCarrello.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollCarrello.setPrefWidth(300);
        scrollCarrello.setMinWidth(280);

        //Layout generale
        root.setLeft(listaProdotti);
        root.setCenter(dettagliBox);
        root.setRight(scrollCarrello);
    }

    public BorderPane getRoot() { return root; }
    public ListView<String> getListaProdotti() { return listaProdotti; }
    public VBox getDettagliBox() { return dettagliBox; }
    public ImageView getImageView() { return imageView; }
    public Label getDisponibilitaLabel() { return disponibilitaLabel; }
    public TextField getQuantitaField() { return quantitaField; }
    public Button getAggiungiButton() { return aggiungiButton; }

    public VBox getCarrelloBox() { return carrelloBox; }
    public VBox getRigheCarrelloBox() { return righeCarrelloBox; }
    public Label getTotaleLabel() { return totaleLabel; }

    public Button getInviaOrdineButton() { return inviaOrdineButton; }
    public Button getLogoutButton() { return logoutButton; }
    public Button getProfiloButton() { return profiloButton; }
    public Button getVisualizzaPagamentiButton() {
        return visualizzaPagamentiButton;
    }
}
