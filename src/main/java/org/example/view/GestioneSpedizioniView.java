package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.example.service.ClienteRiepilogoRow;
import org.example.service.OrdineTableRow;

public class GestioneSpedizioniView {

    private final VBox root;

    private final TableView<OrdineTableRow> ordiniTable;
    private final TextField codiceSpedizioneField;
    private final Button segnaSpeditoButton;
    private final Button aggiornaButton;
    private final Button tornaIndietroButton;

    private final TextField campoImportoBonifico;
    private final Button registraBonificoButton;
    private final ComboBox<String> clienteBonificoComboBox;

    private final TableView<ClienteRiepilogoRow> tabellaRiepilogoClienti;

    public GestioneSpedizioniView() {
        root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("📦 Gestione Spedizioni");
        title.setFont(new Font(22));

        // Tabella ordini
        ordiniTable = new TableView<>();
        ordiniTable.setPrefHeight(300);
        ordiniTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<OrdineTableRow, String> clienteCol = new TableColumn<>("Cliente");
        TableColumn<OrdineTableRow, String> dataCol = new TableColumn<>("Data");
        TableColumn<OrdineTableRow, String> prodottiCol = new TableColumn<>("Prodotti");
        TableColumn<OrdineTableRow, Number> importoCol = new TableColumn<>("Totale");
        TableColumn<OrdineTableRow, String> statoCol = new TableColumn<>("Stato");
        TableColumn<OrdineTableRow, String> indirizzoCol = new TableColumn<>("Indirizzo");

        clienteCol.setCellValueFactory(c -> c.getValue().clienteProperty());
        dataCol.setCellValueFactory(c -> c.getValue().dataProperty());
        prodottiCol.setCellValueFactory(c -> c.getValue().prodottiProperty());
        importoCol.setCellValueFactory(c -> c.getValue().importoProperty());
        statoCol.setCellValueFactory(c -> c.getValue().statoProperty());
        indirizzoCol.setCellValueFactory(c -> c.getValue().indirizzoProperty());

        ordiniTable.getColumns().addAll(clienteCol, dataCol, prodottiCol, importoCol, statoCol, indirizzoCol);

        // Campo codice spedizione
        codiceSpedizioneField = new TextField();
        codiceSpedizioneField.setPromptText("Codice di spedizione");

        segnaSpeditoButton = new Button("Segna come Spedito");
        aggiornaButton = new Button("Aggiorna Ordini");
        tornaIndietroButton = new Button("Torna alla Gestione");

        HBox azioniBox = new HBox(10, codiceSpedizioneField, segnaSpeditoButton);
        azioniBox.setPadding(new Insets(10, 0, 10, 0));

        Label pagamentoLabel = new Label("Pagamento Ricevuto");

        campoImportoBonifico = new TextField();
        campoImportoBonifico.setPromptText("Importo bonificato");

        clienteBonificoComboBox = new ComboBox<>();
        clienteBonificoComboBox.setPromptText("Seleziona cliente");

        registraBonificoButton = new Button("Registra Bonifico");

        VBox bonificoBox = new VBox(8,
                pagamentoLabel,
                clienteBonificoComboBox,
                campoImportoBonifico,
                registraBonificoButton
        );
        bonificoBox.setStyle("-fx-border-color: gray; -fx-padding: 10; -fx-background-color: #f9f9f9;");

        tabellaRiepilogoClienti = new TableView<>();
        tabellaRiepilogoClienti.setPrefHeight(200);
        tabellaRiepilogoClienti.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<ClienteRiepilogoRow, String> usernameCol = new TableColumn<>("Cliente");
        usernameCol.setCellValueFactory(c -> c.getValue().usernameProperty());

        TableColumn<ClienteRiepilogoRow, Number> totaleOrdiniCol = new TableColumn<>("Totale Ordini");
        totaleOrdiniCol.setCellValueFactory(c -> c.getValue().totaleOrdiniProperty());

        TableColumn<ClienteRiepilogoRow, Number> pagatoCol = new TableColumn<>("Pagato");
        pagatoCol.setCellValueFactory(c -> c.getValue().totalePagatoProperty());

        TableColumn<ClienteRiepilogoRow, Number> residuoCol = new TableColumn<>("Residuo");
        residuoCol.setCellValueFactory(c -> c.getValue().residuoProperty());

        tabellaRiepilogoClienti.getColumns().addAll(usernameCol, totaleOrdiniCol, pagatoCol, residuoCol);

        root.getChildren().addAll(
                title,
                ordiniTable,
                azioniBox,
                aggiornaButton,
                bonificoBox,
                new Label("🧾 Riepilogo Debiti Clienti"),
                tabellaRiepilogoClienti,
                tornaIndietroButton
        );
    }


    public VBox getRoot() {
        return root;
    }

    public TableView<OrdineTableRow> getOrdiniTable() {
        return ordiniTable;
    }

    public TableView<ClienteRiepilogoRow> getTabellaRiepilogoClienti() {
        return tabellaRiepilogoClienti;
    }

    public TextField getCodiceSpedizioneField() {
        return codiceSpedizioneField;
    }

    public Button getSegnaSpeditoButton() {
        return segnaSpeditoButton;
    }

    public Button getAggiornaButton() {
        return aggiornaButton;
    }

    public Button getTornaIndietroButton() {
        return tornaIndietroButton;
    }

    public TextField getCampoImportoBonifico() {
        return campoImportoBonifico;
    }

    public Button getRegistraBonificoButton() {
        return registraBonificoButton;
    }

    public ComboBox<String> getClienteBonificoComboBox() {
        return clienteBonificoComboBox;
    }
}