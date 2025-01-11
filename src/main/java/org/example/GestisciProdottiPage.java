package org.example;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.*;

public class GestisciProdottiPage {

    // Dichiarazione di magazzinoTable e negozioTable come variabili di classe
    private TableView<Product> magazzinoTable;
    private TableView<Product> negozioTable;

    private static final String MAGAZZINO = "Magazzino";
    private static final String NEGOZIO = "Negozio";
    private static final String QUANTITA = "quantita";
    private static final String SCAFFALE = "scaffale";
    private static final String CODICE_A_BARRE = "codice_a_barre";
    private static final String SOGLIA = "soglia";
    private static final String PREZZO_ACQUISTO = "prezzo_acquisto";
    private static final String PREZZO_VENDITA  = "prezzo_vendita";


    public void start(Stage primaryStage) {
        Button backButton = new Button("Torna alla Pagina Prima");
        backButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.showMainPage(primaryStage);
        });

        TextField searchField = new TextField();
        searchField.setPromptText("Cerca un prodotto...");

        // Creazione di un'istanza di ProductTable
        ProductTable productTable = new ProductTable();

// Utilizzare l'istanza per creare le tabelle
        magazzinoTable = productTable.createProductTable(this);
        negozioTable = productTable.createProductTable(this);


        Button gestioneButton = new Button("Gestione");
        gestioneButton.setOnAction(e -> showGestionePage(primaryStage));

        loadProducts(magazzinoTable, MAGAZZINO);
        loadProducts(negozioTable, NEGOZIO);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchProducts(newValue, magazzinoTable, MAGAZZINO);
            searchProducts(newValue, negozioTable, NEGOZIO);
        });

        VBox vbox = new VBox(15, backButton, gestioneButton, searchField,
                new Label(MAGAZZINO), magazzinoTable,
                new Label(NEGOZIO), negozioTable);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(vbox, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void loadProducts(TableView<Product> table, String categoria) {
        if (!LoginPage.isOffline && InternetCheck.isConnected()) {
            try (Connection conn = DatabaseConnection.connectToDatabase()) {
                String query = "SELECT * FROM prodotti WHERE categoria = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, categoria);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            Product product = new Product(
                                    rs.getString("nome"),
                                    rs.getInt(QUANTITA),
                                    rs.getString(SCAFFALE),
                                    rs.getString(CODICE_A_BARRE),
                                    rs.getInt(SOGLIA),
                                    rs.getDouble(PREZZO_ACQUISTO),
                                    rs.getDouble(PREZZO_VENDITA),
                                    rs.getBytes("immagine")
                            );
                            table.getItems().add(product);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Label offlineLabel = new Label("Connessione non disponibile, i dati sono locali.");
            table.getItems().clear();
            table.getItems().add(new Product("Nessun dato disponibile", 0, "-", "-", 0, 0.0, 0.0, null));
        }
    }

    private void searchProducts(String searchTerm, TableView<Product> table, String categoria) {
        table.getItems().clear();
        if (!LoginPage.isOffline && InternetCheck.isConnected()) {
            try (Connection conn = DatabaseConnection.connectToDatabase()) {
                String query = "SELECT * FROM prodotti WHERE (nome LIKE ? OR scaffale LIKE ? OR codice_a_barre LIKE ?) AND categoria = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    String searchPattern = "%" + searchTerm + "%";
                    stmt.setString(1, searchPattern);
                    stmt.setString(2, searchPattern);
                    stmt.setString(3, searchPattern);
                    stmt.setString(4, categoria);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            Product product = new Product(
                                    rs.getString("nome"),
                                    rs.getInt(QUANTITA),
                                    rs.getString(SCAFFALE),
                                    rs.getString(CODICE_A_BARRE),
                                    rs.getInt(SOGLIA),
                                    rs.getDouble(PREZZO_ACQUISTO),
                                    rs.getDouble(PREZZO_VENDITA),
                                    rs.getBytes("immagine")
                            );
                            table.getItems().add(product);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            table.getItems().add(new Product("Nessun dato trovato", 0, "-", "-", 0, 0.0, 0.0, null));
        }
    }

    private void showGestionePage(Stage primaryStage) {
        GestionePage gestionePage = new GestionePage();
        gestionePage.start(primaryStage);
    }

    // Elimina il prodotto dal database
    public void deleteProductFromDatabase(Product product) {
        if (!LoginPage.isOffline && InternetCheck.isConnected()) {
            try (Connection conn = DatabaseConnection.connectToDatabase()) {
                String query = "DELETE FROM prodotti WHERE codice_a_barre = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, product.getCodice_a_barre());
                    stmt.executeUpdate();
                    // Refresh table after deletion
                    refreshTable();  // This reloads the data after deletion
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Apre il dialogo per modificare il prodotto
    public void openEditProductDialog(Product product) {
        TextField nameField = new TextField(product.getNome());
        TextField quantityField = new TextField(String.valueOf(product.getQuantita()));
        TextField shelfField = new TextField(product.getScaffale());
        TextField barcodeField = new TextField(product.getCodice_a_barre());
        TextField thresholdField = new TextField(String.valueOf(product.getSoglia()));
        TextField purchasePriceField = new TextField(String.valueOf(product.getPrezzo_acquisto()));
        TextField salePriceField = new TextField(String.valueOf(product.getPrezzo_vendita()));

        // Dialogo per la modifica
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifica Prodotto");

        ButtonType saveButtonType = new ButtonType("Salva", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Quantità:"), 0, 1);
        grid.add(quantityField, 1, 1);
        grid.add(new Label("Scaffale:"), 0, 2);
        grid.add(shelfField, 1, 2);
        grid.add(new Label("Codice a Barre:"), 0, 3);
        grid.add(barcodeField, 1, 3);
        grid.add(new Label("Soglia:"), 0, 4);
        grid.add(thresholdField, 1, 4);
        grid.add(new Label("Prezzo Acquisto:"), 0, 5);
        grid.add(purchasePriceField, 1, 5);
        grid.add(new Label("Prezzo Vendita:"), 0, 6);
        grid.add(salePriceField, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                // Aggiorna il prodotto con i nuovi valori
                product.setNome(nameField.getText());
                product.setQuantita(Integer.parseInt(quantityField.getText()));
                product.setScaffale(shelfField.getText());
                product.setCodice_a_barre(barcodeField.getText());
                product.setSoglia(Integer.parseInt(thresholdField.getText()));
                product.setPrezzo_acquisto(Double.parseDouble(purchasePriceField.getText()));
                product.setPrezzo_vendita(Double.parseDouble(salePriceField.getText()));
                saveProductToDatabase(product);
            }
            return null;
        });

        dialog.showAndWait();
    }

    // Metodo per salvare le modifiche nel database
    private void saveProductToDatabase(Product product) {
        if (!LoginPage.isOffline && InternetCheck.isConnected()) {
            try (Connection conn = DatabaseConnection.connectToDatabase()) {
                String query = "UPDATE prodotti SET nome = ?, quantita = ?, scaffale = ?, codice_a_barre = ?, soglia = ?, prezzo_acquisto = ?, prezzo_vendita = ? WHERE codice_a_barre = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, product.getNome());
                    stmt.setInt(2, product.getQuantita());
                    stmt.setString(3, product.getScaffale());
                    stmt.setString(4, product.getCodice_a_barre());
                    stmt.setInt(5, product.getSoglia());
                    stmt.setDouble(6, product.getPrezzo_acquisto());
                    stmt.setDouble(7, product.getPrezzo_vendita());
                    stmt.setString(8, product.getCodice_a_barre());
                    stmt.executeUpdate();
                    refreshTable();  // Ricarica la tabella dopo la modifica
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Ricarica la tabella dei prodotti dopo modifiche o eliminazioni
    public void refreshTable() {
        magazzinoTable.getItems().clear();
        negozioTable.getItems().clear();
        loadProducts(magazzinoTable, MAGAZZINO);  // Ricarica i prodotti per la tabella Magazzino
        loadProducts(negozioTable, "Negozio");     // Ricarica i prodotti per la tabella Negozio
    }

    public static class Product {
        private final SimpleStringProperty nome;
        private final SimpleIntegerProperty quantita;
        private final SimpleStringProperty scaffale;
        private final SimpleStringProperty codice_a_barre;
        private final SimpleIntegerProperty soglia;
        private final SimpleDoubleProperty prezzo_acquisto;
        private final SimpleDoubleProperty prezzo_vendita;
        private final SimpleObjectProperty<byte[]> immagine;

        public Product(String nome, int quantita, String scaffale, String codice_a_barre, int soglia, double prezzo_acquisto, double prezzo_vendita, byte[] immagine) {
            this.nome = new SimpleStringProperty(nome);
            this.quantita = new SimpleIntegerProperty(quantita);
            this.scaffale = new SimpleStringProperty(scaffale);
            this.codice_a_barre = new SimpleStringProperty(codice_a_barre);
            this.soglia = new SimpleIntegerProperty(soglia);
            this.prezzo_acquisto = new SimpleDoubleProperty(prezzo_acquisto);
            this.prezzo_vendita = new SimpleDoubleProperty(prezzo_vendita);
            this.immagine = new SimpleObjectProperty<>(immagine);
        }

        public String getNome() {
            return nome.get();
        }

        public void setNome(String nome) {
            this.nome.set(nome);
        }

        public int getQuantita() {
            return quantita.get();
        }

        public void setQuantita(int quantita) {
            this.quantita.set(quantita);
        }

        public String getScaffale() {
            return scaffale.get();
        }

        public void setScaffale(String scaffale) {
            this.scaffale.set(scaffale);
        }

        public String getCodice_a_barre() {
            return codice_a_barre.get();
        }

        public void setCodice_a_barre(String codice_a_barre) {
            this.codice_a_barre.set(codice_a_barre);
        }

        public int getSoglia() {
            return soglia.get();
        }

        public void setSoglia(int soglia) {
            this.soglia.set(soglia);
        }

        public double getPrezzo_acquisto() {
            return prezzo_acquisto.get();
        }

        public void setPrezzo_acquisto(double prezzo_acquisto) {
            this.prezzo_acquisto.set(prezzo_acquisto);
        }

        public double getPrezzo_vendita() {
            return prezzo_vendita.get();
        }

        public void setPrezzo_vendita(double prezzo_vendita) {
            this.prezzo_vendita.set(prezzo_vendita);
        }

        public byte[] getImmagine() {
            return immagine.get();
        }

        public void setImmagine(byte[] immagine) {
            this.immagine.set(immagine);
        }
    }
}
