package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

public class MainGUI extends Application {

    // Definizione della costante
    private static final String CAMPI_VUOTI_MSG = "Tutti i campi devono essere riempiti!";

    private ObservableList<Prodotto> prodotti;
    private GestioneFile gestioneFile;

    // Variabili globali per i campi di input
    private TextField nomeField, quantitaField, scaffaleField, codiceBarreField, sogliaField, prezzoAcquistoField, prezzoVenditaField;
    private TextArea avvisiArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        gestioneFile = new GestioneFile("prodotti.txt");
        prodotti = FXCollections.observableArrayList(gestioneFile.leggiProdotti());

        // Setup TableView
        TableView<Prodotto> table = new TableView<>();
        TableColumn<Prodotto, String> nomeColumn = new TableColumn<>("Nome");
        nomeColumn.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());

        TableColumn<Prodotto, Integer> quantitaColumn = new TableColumn<>("Quantità");
        quantitaColumn.setCellValueFactory(cellData -> cellData.getValue().quantitaProperty().asObject());

        TableColumn<Prodotto, String> scaffaleColumn = new TableColumn<>("Scaffale");
        scaffaleColumn.setCellValueFactory(cellData -> cellData.getValue().scaffaleProperty());

        TableColumn<Prodotto, String> codiceBarreColumn = new TableColumn<>("Codice a Barre");
        codiceBarreColumn.setCellValueFactory(cellData -> cellData.getValue().codiceBarreProperty());

        TableColumn<Prodotto, Double> prezzoAcquistoColumn = new TableColumn<>("Prezzo Acquisto");
        prezzoAcquistoColumn.setCellValueFactory(cellData -> cellData.getValue().prezzoAcquistoProperty().asObject());

        TableColumn<Prodotto, Double> prezzoVenditaColumn = new TableColumn<>("Prezzo Vendita");
        prezzoVenditaColumn.setCellValueFactory(cellData -> cellData.getValue().prezzoVenditaProperty().asObject());

        table.getColumns().addAll(nomeColumn, quantitaColumn, scaffaleColumn, codiceBarreColumn, prezzoAcquistoColumn, prezzoVenditaColumn);
        table.setItems(prodotti);

        // Dichiarazione separata delle variabili
        nomeField = new TextField();
        quantitaField = new TextField();
        scaffaleField = new TextField();
        codiceBarreField = new TextField();
        sogliaField = new TextField();
        prezzoAcquistoField = new TextField();
        prezzoVenditaField = new TextField();
        avvisiArea = new TextArea();
        avvisiArea.setEditable(false);

        // Buttons
        Button aggiungiButton = new Button("Aggiungi Prodotto");
        Button rimuoviButton = new Button("Rimuovi Prodotto");
        Button cercaButton = new Button("Cerca Prodotto");
        Button aggiornaSogliaButton = new Button("Aggiorna Soglia");
        Button aggiornaPrezzoButton = new Button("Aggiorna Prezzo");

        // Event handlers
        aggiungiButton.setOnAction(e -> aggiungiProdotto());
        rimuoviButton.setOnAction(e -> rimuoviProdotto());
        cercaButton.setOnAction(e -> cercaProdotto());
        aggiornaSogliaButton.setOnAction(e -> aggiornaSoglia());
        aggiornaPrezzoButton.setOnAction(e -> aggiornaPrezzo());

        // Form layout
        HBox hboxButtons = new HBox(10, aggiungiButton, rimuoviButton, cercaButton, aggiornaSogliaButton, aggiornaPrezzoButton);
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("Nome:"), nomeField,
                new Label("Quantità:"), quantitaField,
                new Label("Scaffale:"), scaffaleField,
                new Label("Codice a barre:"), codiceBarreField,
                new Label("Soglia:"), sogliaField,
                new Label("Prezzo Acquisto:"), prezzoAcquistoField,
                new Label("Prezzo Vendita:"), prezzoVenditaField,
                hboxButtons,
                new Label("Avvisi:"), avvisiArea,
                table
        );

        // Scene setup
        Scene scene = new Scene(vbox, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Gestione Prodotti");
        stage.show();
    }

    private void aggiungiProdotto() {
        String nome = nomeField.getText();
        String quantitaText = quantitaField.getText();
        String scaffale = scaffaleField.getText();
        String codiceBarre = codiceBarreField.getText();
        String prezzoAcquistoText = prezzoAcquistoField.getText();
        String prezzoVenditaText = prezzoVenditaField.getText();
        String sogliaText = sogliaField.getText();

        // Controllo se i campi numerici sono validi
        if (nome.isEmpty() || quantitaText.isEmpty() || scaffale.isEmpty() || codiceBarre.isEmpty() || prezzoAcquistoText.isEmpty() || prezzoVenditaText.isEmpty() || sogliaText.isEmpty()) {
            JOptionPane.showMessageDialog(null, CAMPI_VUOTI_MSG);
            return;
        }

        // Prova a convertire la quantità
        int quantita;
        try {
            quantita = Integer.parseInt(quantitaText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La quantità deve essere un numero valido!");
            return;
        }

        // Prova a convertire i prezzi
        double prezzoAcquisto = 0;
        double prezzoVendita = 0;
        try {
            prezzoAcquisto = Double.parseDouble(prezzoAcquistoText);
            prezzoVendita = Double.parseDouble(prezzoVenditaText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "I prezzi devono essere numeri validi!");
            return;
        }

        // Prova a convertire la soglia
        int soglia;
        try {
            soglia = Integer.parseInt(sogliaText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La soglia deve essere un numero valido!");
            return;
        }

        // Crea il nuovo prodotto
        Prodotto prodotto = new Prodotto(nome, quantita, scaffale, codiceBarre, prezzoAcquisto, prezzoVendita);
        prodotto.setSoglia(soglia);
        prodotti.add(prodotto);
        gestioneFile.scriviProdotti(prodotti);
        clearFields();
    }

    private void rimuoviProdotto() {
        String nome = nomeField.getText();
        for (Prodotto prodotto : prodotti) {
            if (prodotto.getNome().equalsIgnoreCase(nome)) {
                prodotti.remove(prodotto);
                gestioneFile.scriviProdotti(prodotti);
                break;
            }
        }
        clearFields();
    }

    private void cercaProdotto() {
        String nome = nomeField.getText();
        StringBuilder risultati = new StringBuilder();
        for (Prodotto prodotto : prodotti) {
            if (prodotto.getNome().equalsIgnoreCase(nome)) {
                risultati.append(prodotto.toString()).append("\n");
            }
        }
        if (risultati.length() == 0) {
            risultati.append("Prodotto non trovato.");
        }
        avvisiArea.setText(risultati.toString());
    }

    private void aggiornaSoglia() {
        String nome = nomeField.getText();
        String sogliaText = sogliaField.getText();

        // Controllo che la soglia sia un numero valido
        if (nome.isEmpty() || sogliaText.isEmpty()) {
            JOptionPane.showMessageDialog(null, CAMPI_VUOTI_MSG);
            return;
        }

        int soglia;
        try {
            soglia = Integer.parseInt(sogliaText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La soglia deve essere un numero valido!");
            return;
        }

        for (Prodotto prodotto : prodotti) {
            if (prodotto.getNome().equalsIgnoreCase(nome)) {
                prodotto.setSoglia(soglia);
                gestioneFile.scriviProdotti(prodotti);
                break;
            }
        }
        clearFields();
    }

    private void aggiornaPrezzo() {
        String nome = nomeField.getText();
        String prezzoAcquistoText = prezzoAcquistoField.getText();
        String prezzoVenditaText = prezzoVenditaField.getText();

        // Controllo che i campi dei prezzi siano validi
        if (nome.isEmpty() || prezzoAcquistoText.isEmpty() || prezzoVenditaText.isEmpty()) {
            JOptionPane.showMessageDialog(null, CAMPI_VUOTI_MSG);
            return;
        }

        double prezzoAcquisto;
        double prezzoVendita;
        try {
            prezzoAcquisto = Double.parseDouble(prezzoAcquistoText);
            prezzoVendita = Double.parseDouble(prezzoVenditaText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "I prezzi devono essere numeri validi!");
            return;
        }

        for (Prodotto prodotto : prodotti) {
            if (prodotto.getNome().equalsIgnoreCase(nome)) {
                prodotto.setPrezzoAcquisto(prezzoAcquisto);
                prodotto.setPrezzoVendita(prezzoVendita);
                gestioneFile.scriviProdotti(prodotti);
                break;
            }
        }
        clearFields();
    }

    private void clearFields() {
        nomeField.clear();
        quantitaField.clear();
        scaffaleField.clear();
        codiceBarreField.clear();
        sogliaField.clear();
        prezzoAcquistoField.clear();
        prezzoVenditaField.clear();
    }
}
