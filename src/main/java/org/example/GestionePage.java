package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.util.List;
import javafx.stage.FileChooser;

public class GestionePage {

    public void start(Stage primaryStage) {
        final Stage finalPrimaryStage = primaryStage;

        Button backButton = new Button("Torna alla Gestione Prodotti");
        backButton.setOnAction(e -> {
            GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
            gestisciProdottiPage.start(finalPrimaryStage);
        });

        TextField nomeField = new TextField();
        nomeField.setPromptText("Nome prodotto");

        TextField scaffaleField = new TextField();
        scaffaleField.setPromptText("Scaffale");

        TextField codiceBarreField = new TextField();
        codiceBarreField.setPromptText("Codice a Barre");

        TextField quantitaField = new TextField();
        quantitaField.setPromptText("Quantità");

        TextField sogliaField = new TextField();
        sogliaField.setPromptText("Soglia");

        TextField prezzoAcquistoField = new TextField();
        prezzoAcquistoField.setPromptText("Prezzo Acquisto");

        TextField prezzoVenditaField = new TextField();
        prezzoVenditaField.setPromptText("Prezzo Vendita");

        final List<File>[] imageFiles = new List[1];

        Button uploadButton = new Button("Carica Immagine");
        Label fileLabel = new Label();

        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            imageFiles[0] = fileChooser.showOpenMultipleDialog(finalPrimaryStage);

            if (imageFiles[0] != null && !imageFiles[0].isEmpty()) {
                fileLabel.setText("File aggiunto: " + imageFiles[0].get(0).getName());
            }
        });

        Button aggiungiButton = new Button("Aggiungi Prodotto");
        aggiungiButton.setOnAction(e -> {
            if (inputValido(nomeField, codiceBarreField, scaffaleField, quantitaField, prezzoAcquistoField, prezzoVenditaField)) {
                byte[] imageBytes = caricaImmagine(imageFiles[0]);
                eseguiOperazioneDatabase(nomeField, codiceBarreField, scaffaleField, quantitaField, sogliaField, prezzoAcquistoField, prezzoVenditaField, imageBytes);
            }
        });

        VBox vbox = new VBox(15, backButton, nomeField, scaffaleField, codiceBarreField, quantitaField, sogliaField, prezzoAcquistoField, prezzoVenditaField, uploadButton, fileLabel, aggiungiButton);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(vbox, 500, 400);
        finalPrimaryStage.setScene(scene);
        finalPrimaryStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Metodo per verificare la validità dell'input
    private boolean inputValido(TextField nomeField, TextField codiceBarreField, TextField scaffaleField, TextField quantitaField, TextField prezzoAcquistoField, TextField prezzoVenditaField) {
        if (nomeField.getText().isEmpty() && codiceBarreField.getText().isEmpty()) {
            showAlert("Errore", "Devi inserire nome o codice a barre!");
            return false;
        }
        if ((scaffaleField.getText().isEmpty() && quantitaField.getText().isEmpty()) ||
                (prezzoAcquistoField.getText().isEmpty() && prezzoVenditaField.getText().isEmpty())) {
            showAlert("Errore", "Sia scaffale, quantità e almeno un prezzo sono obbligatori!");
            return false;
        }
        return true;
    }

    // Metodo per caricare l'immagine
    private byte[] caricaImmagine(List<File> imageFiles) {
        byte[] imageBytes = null;
        if (imageFiles != null && !imageFiles.isEmpty()) {
            try {
                File file = imageFiles.get(0);
                BufferedImage bufferedImage = ImageIO.read(file);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);
                imageBytes = byteArrayOutputStream.toByteArray();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return imageBytes;
    }

    // Metodo per eseguire l'operazione di database (aggiornamento o inserimento)
    private void eseguiOperazioneDatabase(TextField nomeField, TextField codiceBarreField, TextField scaffaleField, TextField quantitaField, TextField sogliaField, TextField prezzoAcquistoField, TextField prezzoVenditaField, byte[] imageBytes) {
        try (Connection conn = DatabaseConnection.connectToDatabase()) {
            String query = "SELECT nome, codice_a_barre FROM prodotti WHERE nome = ? OR codice_a_barre = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nomeField.getText());
                stmt.setString(2, codiceBarreField.getText());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        aggiornaProdotto(conn, quantitaField, imageBytes, nomeField, codiceBarreField);
                    } else {
                        inserisciProdotto(conn, nomeField, scaffaleField, quantitaField, codiceBarreField, sogliaField, prezzoAcquistoField, prezzoVenditaField, imageBytes);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Metodo per aggiornare il prodotto nel database
    private void aggiornaProdotto(Connection conn, TextField quantitaField, byte[] imageBytes, TextField nomeField, TextField codiceBarreField) throws SQLException {
        String updateQuery = "UPDATE prodotti SET quantita = quantita + ?, immagine = ? WHERE nome = ? OR codice_a_barre = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setInt(1, Integer.parseInt(quantitaField.getText()));
            updateStmt.setBytes(2, imageBytes);
            updateStmt.setString(3, nomeField.getText());
            updateStmt.setString(4, codiceBarreField.getText());
            updateStmt.executeUpdate();
        }
    }

    // Metodo per inserire un nuovo prodotto nel database
    private void inserisciProdotto(Connection conn, TextField nomeField, TextField scaffaleField, TextField quantitaField, TextField codiceBarreField, TextField sogliaField, TextField prezzoAcquistoField, TextField prezzoVenditaField, byte[] imageBytes) throws SQLException {
        String insertQuery = "INSERT INTO prodotti (nome, quantita, scaffale, codice_a_barre, soglia, prezzo_acquisto, prezzo_vendita, immagine) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setString(1, nomeField.getText());
            insertStmt.setInt(2, Integer.parseInt(quantitaField.getText()));
            insertStmt.setString(3, scaffaleField.getText());
            insertStmt.setString(4, codiceBarreField.getText());
            insertStmt.setInt(5, Integer.parseInt(sogliaField.getText()));
            insertStmt.setDouble(6, Double.parseDouble(prezzoAcquistoField.getText()));
            insertStmt.setDouble(7, Double.parseDouble(prezzoVenditaField.getText()));
            insertStmt.setBytes(8, imageBytes);
            insertStmt.executeUpdate();
        }
    }
}
