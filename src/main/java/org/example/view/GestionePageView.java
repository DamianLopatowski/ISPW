package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GestionePageView {
    private Stage primaryStage;
    private Button backButton;
    private TextField nomeField;
    private TextField scaffaleField;
    private TextField codiceBarreField;
    private TextField quantitaField;
    private TextField sogliaField;
    private TextField prezzoAcquistoField;
    private TextField prezzoVenditaField;
    private Button uploadButton;
    private Label fileLabel;
    private Button aggiungiButton;

    public GestionePageView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeComponents();
    }

    private void initializeComponents() {
        backButton = new Button("Torna alla Gestione Prodotti");

        nomeField = new TextField();
        nomeField.setPromptText("Nome prodotto");

        scaffaleField = new TextField();
        scaffaleField.setPromptText("Scaffale");

        codiceBarreField = new TextField();
        codiceBarreField.setPromptText("Codice a Barre");

        quantitaField = new TextField();
        quantitaField.setPromptText("Quantità");

        sogliaField = new TextField();
        sogliaField.setPromptText("Soglia");

        prezzoAcquistoField = new TextField();
        prezzoAcquistoField.setPromptText("Prezzo Acquisto");

        prezzoVenditaField = new TextField();
        prezzoVenditaField.setPromptText("Prezzo Vendita");

        uploadButton = new Button("Carica Immagine");
        fileLabel = new Label();

        aggiungiButton = new Button("Aggiungi Prodotto");
    }

    public void setupLayout() {
        VBox vbox = new VBox(15, backButton, nomeField, scaffaleField, codiceBarreField, quantitaField, sogliaField, prezzoAcquistoField, prezzoVenditaField, uploadButton, fileLabel, aggiungiButton);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(vbox, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public Button getBackButton() {
        return backButton;
    }

    public TextField getNomeField() {
        return nomeField;
    }

    public TextField getScaffaleField() {
        return scaffaleField;
    }

    public TextField getCodiceBarreField() {
        return codiceBarreField;
    }

    public TextField getQuantitaField() {
        return quantitaField;
    }

    public TextField getSogliaField() {
        return sogliaField;
    }

    public TextField getPrezzoAcquistoField() {
        return prezzoAcquistoField;
    }

    public TextField getPrezzoVenditaField() {
        return prezzoVenditaField;
    }

    public Button getUploadButton() {
        return uploadButton;
    }

    public Label getFileLabel() {
        return fileLabel;
    }

    public Button getAggiungiButton() {
        return aggiungiButton;
    }
}
