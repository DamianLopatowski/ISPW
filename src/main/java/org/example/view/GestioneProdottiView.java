package org.example.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import org.example.bean.ProdottoBean;
import org.example.service.ProdottoTableRow;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class GestioneProdottiView {
    private final VBox root;

    private final TextField nomeField;
    private final TextField quantitaField;
    private final TextField scaffaleField;
    private final TextField codiceAbarreField;
    private final TextField sogliaField;
    private final TextField prezzoAcquistoField;
    private final TextField prezzoVenditaField;

    private final ComboBox<String> categoriaCombo;

    private final Button selezionaImmagineButton;
    private final Button aggiungiProdottoButton;
    private final Button eliminaProdottoButton;
    private final Button tornaIndietroButton;

    private final ImageView immaginePreview;

    private final TableView<ProdottoTableRow> tabellaProdotti;

    private final TextField modificaQuantitaField;

    private final Button aumentaQuantitaButton;
    private final Button diminuisciQuantitaButton;

    private byte[] immagineSelezionata;

    public GestioneProdottiView() {
        root = new VBox(10);
        root.setPadding(new Insets(20));

        nomeField = new TextField();
        quantitaField = new TextField();
        scaffaleField = new TextField();
        codiceAbarreField = new TextField();
        sogliaField = new TextField();
        prezzoAcquistoField = new TextField();
        prezzoVenditaField = new TextField();

        categoriaCombo = new ComboBox<>();
        categoriaCombo.getItems().addAll("negozio", "magazzino");

        selezionaImmagineButton = new Button("Seleziona immagine");
        immaginePreview = new ImageView();
        immaginePreview.setFitHeight(100);
        immaginePreview.setFitWidth(100);
        immaginePreview.setPreserveRatio(true);

        selezionaImmagineButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleziona immagine");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg", "*.gif")
            );
            File file = fileChooser.showOpenDialog(root.getScene().getWindow());
            if (file != null) {
                try {
                    byte[] bytes = Files.readAllBytes(file.toPath());
                    setImmagine(bytes);
                } catch (IOException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Errore durante il caricamento dell'immagine: " + ex.getMessage());
                    alert.showAndWait();
                }
            }
        });

        aggiungiProdottoButton = new Button("+ Aggiungi prodotto");
        eliminaProdottoButton = new Button("x Elimina prodotto");
        tornaIndietroButton = new Button("<- Torna alla gestione");

        GridPane form = new GridPane();
        form.setVgap(5);
        form.setHgap(10);
        form.add(new Label("Nome:"), 0, 0);       form.add(nomeField, 1, 0);
        form.add(new Label("Quantità:"), 0, 1);   form.add(quantitaField, 1, 1);
        form.add(new Label("Scaffale:"), 0, 2);   form.add(scaffaleField, 1, 2);
        form.add(new Label("Codice a barre:"), 0, 3); form.add(codiceAbarreField, 1, 3);
        form.add(new Label("Soglia:"), 0, 4);     form.add(sogliaField, 1, 4);
        form.add(new Label("Prezzo acquisto:"), 0, 5); form.add(prezzoAcquistoField, 1, 5);
        form.add(new Label("Prezzo vendita:"), 0, 6);  form.add(prezzoVenditaField, 1, 6);
        form.add(new Label("Categoria:"), 0, 7);  form.add(categoriaCombo, 1, 7);
        form.add(selezionaImmagineButton, 1, 8);  form.add(immaginePreview, 2, 0, 1, 4);

        tabellaProdotti = new TableView<>();
        tabellaProdotti.setPrefHeight(300);
        tabellaProdotti.setPlaceholder(new Label("Nessun prodotto disponibile"));

        TableColumn<ProdottoTableRow, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(cell -> cell.getValue().nomeProperty());

        TableColumn<ProdottoTableRow, String> quantitaCol = new TableColumn<>("Quantità");
        quantitaCol.setCellValueFactory(cell -> cell.getValue().quantitaProperty());

        TableColumn<ProdottoTableRow, String> scaffaleCol = new TableColumn<>("Scaffale");
        scaffaleCol.setCellValueFactory(cell -> cell.getValue().scaffaleProperty());

        TableColumn<ProdottoTableRow, String> codiceCol = new TableColumn<>("Codice a barre");
        codiceCol.setCellValueFactory(cell -> cell.getValue().codiceAbarreProperty());

        TableColumn<ProdottoTableRow, String> sogliaCol = new TableColumn<>("Soglia");
        sogliaCol.setCellValueFactory(cell -> cell.getValue().sogliaProperty());

        TableColumn<ProdottoTableRow, String> prezzoAcqCol = new TableColumn<>("Prezzo Acquisto");
        prezzoAcqCol.setCellValueFactory(cell -> cell.getValue().prezzoAcquistoProperty());

        TableColumn<ProdottoTableRow, String> prezzoVendCol = new TableColumn<>("Prezzo Vendita");
        prezzoVendCol.setCellValueFactory(cell -> cell.getValue().prezzoVenditaProperty());

        TableColumn<ProdottoTableRow, String> categoriaCol = new TableColumn<>("Categoria");
        categoriaCol.setCellValueFactory(cell -> cell.getValue().categoriaProperty());

        TableColumn<ProdottoTableRow, ImageView> immagineCol = new TableColumn<>("Immagine");
        immagineCol.setCellValueFactory(cell -> cell.getValue().immagineProperty());

        tabellaProdotti.getColumns().addAll(
                nomeCol, quantitaCol, scaffaleCol, codiceCol, sogliaCol,
                prezzoAcqCol, prezzoVendCol, categoriaCol, immagineCol
        );

        modificaQuantitaField = new TextField();
        modificaQuantitaField.setPromptText("Quantità");
        modificaQuantitaField.setMaxWidth(80);

        aumentaQuantitaButton = new Button("+");
        diminuisciQuantitaButton = new Button("-");

        HBox modificaBox = new HBox(5, modificaQuantitaField, aumentaQuantitaButton, diminuisciQuantitaButton, eliminaProdottoButton);

        root.getChildren().addAll(form, new HBox(10, aggiungiProdottoButton), tabellaProdotti, modificaBox, tornaIndietroButton);
    }

    public VBox getRoot() {
        return root;
    }

    public Button getAggiungiProdottoButton() {
        return aggiungiProdottoButton;
    }

    public Button getEliminaProdottoButton() {
        return eliminaProdottoButton;
    }

    public Button getTornaIndietroButton() {
        return tornaIndietroButton;
    }

    public TextField getModificaQuantitaField() {
        return modificaQuantitaField;
    }

    public Button getAumentaQuantitaButton() {
        return aumentaQuantitaButton;
    }

    public Button getDiminuisciQuantitaButton() {
        return diminuisciQuantitaButton;
    }

    public void mostraProdotti(List<ProdottoBean> prodottiBean) {
        ObservableList<ProdottoTableRow> righe = FXCollections.observableArrayList();
        for (ProdottoBean p : prodottiBean) {
            righe.add(new ProdottoTableRow(p));
        }
        tabellaProdotti.setItems(righe);
    }

    public ProdottoBean creaProdottoDaInput() {
        try {
            return new ProdottoBean.Builder()
                    .nome(nomeField.getText())
                    .quantita(Integer.parseInt(quantitaField.getText()))
                    .scaffale(scaffaleField.getText())
                    .codiceAbarre(codiceAbarreField.getText())
                    .soglia(Integer.parseInt(sogliaField.getText()))
                    .prezzoAcquisto(Double.parseDouble(prezzoAcquistoField.getText()))
                    .prezzoVendita(Double.parseDouble(prezzoVenditaField.getText()))
                    .categoria(categoriaCombo.getValue())
                    .immagine(immagineSelezionata)
                    .build();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nei campi inseriti: " + e.getMessage());
            alert.showAndWait();
            return null;
        }
    }

    public ProdottoBean getProdottoSelezionato() {
        ProdottoTableRow row = tabellaProdotti.getSelectionModel().getSelectedItem();
        return (row != null) ? row.getProdottoOriginale() : null;
    }

    public void setImmagine(byte[] imgBytes) {
        this.immagineSelezionata = imgBytes;
        if (imgBytes != null) {
            immaginePreview.setImage(new Image(new ByteArrayInputStream(imgBytes)));
        }
    }

    public void pulisciCampiInput() {
        nomeField.clear();
        quantitaField.clear();
        scaffaleField.clear();
        codiceAbarreField.clear();
        sogliaField.clear();
        prezzoAcquistoField.clear();
        prezzoVenditaField.clear();
        categoriaCombo.getSelectionModel().clearSelection();
        immaginePreview.setImage(null);
        immagineSelezionata = null;
    }
}