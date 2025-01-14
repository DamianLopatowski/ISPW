package org.example.controller;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import org.example.database.DatabaseConnection;
import org.example.model.DettagliProdotto;
import org.example.model.Prodotto;
import org.example.model.QuantitaProdotto;
import org.example.ui.GestisciProdottiPage;
import org.example.view.GestionePageView;

import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GestionePageController {
    private final GestionePageView view;
    private final Stage stage;
    private List<File> imageFiles;

    public GestionePageController(GestionePageView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        view.getBackButton().setOnAction(e -> {
            // Tornare alla pagina di gestione prodotti
            GestisciProdottiPage gestisciProdottiPage = new GestisciProdottiPage();
            gestisciProdottiPage.start(stage);
        });

        view.getUploadButton().setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            imageFiles = fileChooser.showOpenMultipleDialog(stage);

            if (imageFiles != null && !imageFiles.isEmpty()) {
                view.getFileLabel().setText("File aggiunto: " + imageFiles.get(0).getName());
            }
        });

        view.getAggiungiButton().setOnAction(e -> {
            if (inputValido()) {
                byte[] imageBytes = caricaImmagine(imageFiles);
                Prodotto prodotto = new Prodotto(
                        view.getNomeField().getText(),
                        view.getScaffaleField().getText(),
                        view.getCodiceBarreField().getText(),
                        new DettagliProdotto(
                                Double.parseDouble(view.getPrezzoAcquistoField().getText()),
                                Double.parseDouble(view.getPrezzoVenditaField().getText())
                        ),
                        new QuantitaProdotto(
                                Integer.parseInt(view.getQuantitaField().getText()),
                                Integer.parseInt(view.getSogliaField().getText())
                        ),
                        imageBytes
                );
                eseguiOperazioneDatabase(prodotto);
            }
        });
    }

    private boolean inputValido() {
        if (view.getNomeField().getText().isEmpty() && view.getCodiceBarreField().getText().isEmpty()) {
            showAlert("Errore", "Devi inserire nome o codice a barre!");
            return false;
        }
        return true;
    }

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

    private void eseguiOperazioneDatabase(Prodotto prodotto) {
        try (Connection conn = DatabaseConnection.connectToDatabase()) {
            String query = "SELECT nome, codiceAbarre FROM prodotti WHERE nome = ? OR codiceAbarre = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, prodotto.getNome());
                stmt.setString(2, prodotto.getCodiceBarre());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        aggiornaProdotto(conn, prodotto);
                    } else {
                        inserisciProdotto(conn, prodotto);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void aggiornaProdotto(Connection conn, Prodotto prodotto) throws SQLException {
        String updateQuery = "UPDATE prodotti SET quantita = quantita + ?, immagine = ? WHERE nome = ? OR codiceAbarre = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
            updateStmt.setInt(1, prodotto.getQuantitaProdotto().getQuantita());
            updateStmt.setBytes(2, prodotto.getImmagine());
            updateStmt.setString(3, prodotto.getNome());
            updateStmt.setString(4, prodotto.getCodiceBarre());
            updateStmt.executeUpdate();
        }
    }

    private void inserisciProdotto(Connection conn, Prodotto prodotto) throws SQLException {
        String insertQuery = "INSERT INTO prodotti (nome, quantita, scaffale, codiceAbarre, soglia, prezzoAcquisto, prezzoVendita, immagine) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
            insertStmt.setString(1, prodotto.getNome());
            insertStmt.setInt(2, prodotto.getQuantitaProdotto().getQuantita());
            insertStmt.setString(3, prodotto.getScaffale());
            insertStmt.setString(4, prodotto.getCodiceBarre());
            insertStmt.setInt(5, prodotto.getQuantitaProdotto().getSoglia());
            insertStmt.setDouble(6, prodotto.getDettagliProdotto().getPrezzoAcquisto());
            insertStmt.setDouble(7, prodotto.getDettagliProdotto().getPrezzoVendita());
            insertStmt.setBytes(8, prodotto.getImmagine());
            insertStmt.executeUpdate();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
