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

public class GestisciProdottiPage implements NavigablePage  {


    // Dichiarazione di magazzinoTable e negozioTable come variabili di classe
    private TableView<Product> magazzinoTable;
    private TableView<Product> negozioTable;

    private static final String MAGAZZINO = "Magazzino";
    private static final String NEGOZIO = "Negozio";
    private static final String QUANTITA = "quantita";
    private static final String SCAFFALE = "scaffale";
    private static final String CODICE_A_BARRE = "codiceAbarre";
    private static final String SOGLIA = "soglia";
    private static final String PREZZO_ACQUISTO = "prezzoAcquisto";
    private static final String PREZZO_VENDITA = "prezzoVendita";

    @Override
    public void start(Stage primaryStage) {
        Button backButton = new Button("Torna alla Gestione Prodotti");
        backButton.setOnAction(e -> {
            // Usa PageNavigator per tornare alla pagina Gestione
            GestionePage gestionePage = new GestionePage();
            gestionePage.start(primaryStage);  // Passa primaryStage correttamente
        });

        TextField searchField = new TextField();
        searchField.setPromptText("Cerca un prodotto...");

        ProductTable productTable = new ProductTable();

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
        if (!LoginPage.isOffline() && InternetCheck.isConnected()) {
            try (Connection conn = DatabaseConnection.connectToDatabase()) {  // Using the static method directly
                String query = "SELECT nome, quantita, scaffale, codiceAbarre, soglia, prezzoAcquisto, prezzoVendita, immagine FROM prodotti WHERE categoria = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, categoria);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            Product product = new Product.Builder(rs.getString("nome"), rs.getInt(QUANTITA))
                                    .scaffale(rs.getString(SCAFFALE))
                                    .codiceAbarre(rs.getString(CODICE_A_BARRE))
                                    .soglia(rs.getInt(SOGLIA))
                                    .prezzoAcquisto(rs.getDouble(PREZZO_ACQUISTO))
                                    .prezzoVendita(rs.getDouble(PREZZO_VENDITA))
                                    .immagine(rs.getBytes("immagine"))
                                    .build();
                            table.getItems().add(product);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            table.getItems().clear();
            table.getItems().add(new Product.Builder("Nessun dato disponibile", 0)
                    .scaffale("-")
                    .codiceAbarre("-")
                    .soglia(0)
                    .prezzoAcquisto(0.0)
                    .prezzoVendita(0.0)
                    .immagine(null)
                    .build());
        }
    }

    private void searchProducts(String searchTerm, TableView<Product> table, String categoria) {
        table.getItems().clear();
        if (!LoginPage.isOffline() && InternetCheck.isConnected()) {
            try (Connection conn = DatabaseConnection.connectToDatabase()) {  // Using the static method directly
                String query = "SELECT nome, quantita, scaffale, codiceAbarre, soglia, prezzoAcquisto, prezzoVendita, immagine FROM prodotti WHERE (nome LIKE ? OR scaffale LIKE ? OR codiceAbarre LIKE ?) AND categoria = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    String searchPattern = "%" + searchTerm + "%";
                    stmt.setString(1, searchPattern);
                    stmt.setString(2, searchPattern);
                    stmt.setString(3, searchPattern);
                    stmt.setString(4, categoria);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            Product product = new Product.Builder(rs.getString("nome"), rs.getInt(QUANTITA))
                                    .scaffale(rs.getString(SCAFFALE))
                                    .codiceAbarre(rs.getString(CODICE_A_BARRE))
                                    .soglia(rs.getInt(SOGLIA))
                                    .prezzoAcquisto(rs.getDouble(PREZZO_ACQUISTO))
                                    .prezzoVendita(rs.getDouble(PREZZO_VENDITA))
                                    .immagine(rs.getBytes("immagine"))
                                    .build();
                            table.getItems().add(product);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            table.getItems().add(new Product.Builder("Nessun dato trovato", 0)
                    .scaffale("-")
                    .codiceAbarre("-")
                    .soglia(0)
                    .prezzoAcquisto(0.0)
                    .prezzoVendita(0.0)
                    .immagine(null)
                    .build());
        }
    }

    private void showGestionePage(Stage primaryStage) {
        GestionePage gestionePage = new GestionePage();
        gestionePage.start(primaryStage);
    }

    public void deleteProductFromDatabase(Product product) {
        if (!LoginPage.isOffline() && InternetCheck.isConnected()) {
            try (Connection conn = DatabaseConnection.connectToDatabase()) {  // Using the static method directly
                String query = "DELETE FROM prodotti WHERE codiceAbarre = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, product.getCodiceAbarre());
                    stmt.executeUpdate();
                    refreshTable();  // Reload the table after deletion
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void openEditProductDialog(Product product) {
        TextField nameField = new TextField(product.getNome());
        TextField quantityField = new TextField(String.valueOf(product.getQuantita()));
        TextField shelfField = new TextField(product.getScaffale());
        TextField barcodeField = new TextField(product.getCodiceAbarre());
        TextField thresholdField = new TextField(String.valueOf(product.getSoglia()));
        TextField purchasePriceField = new TextField(String.valueOf(product.getPrezzoAcquisto()));
        TextField salePriceField = new TextField(String.valueOf(product.getPrezzoVendita()));

        // Dialog for editing the product
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
                // Update product with new values
                product.setNome(nameField.getText());
                product.setQuantita(Integer.parseInt(quantityField.getText()));
                product.setScaffale(shelfField.getText());
                product.setCodiceAbarre(barcodeField.getText());
                product.setSoglia(Integer.parseInt(thresholdField.getText()));
                product.setPrezzoAcquisto(Double.parseDouble(purchasePriceField.getText()));
                product.setPrezzoVendita(Double.parseDouble(salePriceField.getText()));
                saveProductToDatabase(product);
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void saveProductToDatabase(Product product) {
        if (!LoginPage.isOffline() && InternetCheck.isConnected()) {
            try (Connection conn = DatabaseConnection.connectToDatabase()) {  // Using the static method directly
                String query = "UPDATE prodotti SET nome = ?, quantita = ?, scaffale = ?, codiceAbarre = ?, soglia = ?, prezzoAcquisto = ?, prezzoVendita = ? WHERE codiceAbarre = ?";
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, product.getNome());
                    stmt.setInt(2, product.getQuantita());
                    stmt.setString(3, product.getScaffale());
                    stmt.setString(4, product.getCodiceAbarre());
                    stmt.setInt(5, product.getSoglia());
                    stmt.setDouble(6, product.getPrezzoAcquisto());
                    stmt.setDouble(7, product.getPrezzoVendita());
                    stmt.setString(8, product.getCodiceAbarre());
                    stmt.executeUpdate();
                    refreshTable();  // Reload the table after editing
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshTable() {
        magazzinoTable.getItems().clear();
        negozioTable.getItems().clear();
        loadProducts(magazzinoTable, MAGAZZINO);  // Reload products for Magazzino table
        loadProducts(negozioTable, NEGOZIO);     // Reload products for Negozio table
    }

    public static class Product {
        private final SimpleStringProperty nome;
        private final SimpleIntegerProperty quantita;
        private final SimpleStringProperty scaffale;
        private final SimpleStringProperty codiceAbarre;
        private final SimpleIntegerProperty soglia;
        private final SimpleDoubleProperty prezzoAcquisto;
        private final SimpleDoubleProperty prezzoVendita;
        private final SimpleObjectProperty<byte[]> immagine;

        private Product(Builder builder) {
            this.nome = builder.nome;
            this.quantita = builder.quantita;
            this.scaffale = builder.scaffale;
            this.codiceAbarre = builder.codiceAbarre;
            this.soglia = builder.soglia;
            this.prezzoAcquisto = builder.prezzoAcquisto;
            this.prezzoVendita = builder.prezzoVendita;
            this.immagine = builder.immagine;
        }

        public static class Builder {
            private SimpleStringProperty nome;
            private SimpleIntegerProperty quantita;
            private SimpleStringProperty scaffale;
            private SimpleStringProperty codiceAbarre;
            private SimpleIntegerProperty soglia;
            private SimpleDoubleProperty prezzoAcquisto;
            private SimpleDoubleProperty prezzoVendita;
            private SimpleObjectProperty<byte[]> immagine;

            public Builder(String nome, int quantita) {
                this.nome = new SimpleStringProperty(nome);
                this.quantita = new SimpleIntegerProperty(quantita);
            }

            public Builder scaffale(String scaffale) {
                this.scaffale = new SimpleStringProperty(scaffale);
                return this;
            }

            public Builder codiceAbarre(String codiceAbarre) {
                this.codiceAbarre = new SimpleStringProperty(codiceAbarre);
                return this;
            }

            public Builder soglia(int soglia) {
                this.soglia = new SimpleIntegerProperty(soglia);
                return this;
            }

            public Builder prezzoAcquisto(double prezzoAcquisto) {
                this.prezzoAcquisto = new SimpleDoubleProperty(prezzoAcquisto);
                return this;
            }

            public Builder prezzoVendita(double prezzoVendita) {
                this.prezzoVendita = new SimpleDoubleProperty(prezzoVendita);
                return this;
            }

            public Builder immagine(byte[] immagine) {
                this.immagine = new SimpleObjectProperty<>(immagine);
                return this;
            }

            public Product build() {
                return new Product(this);
            }
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

        public String getCodiceAbarre() {
            return codiceAbarre.get();
        }

        public void setCodiceAbarre(String codiceAbarre) {
            this.codiceAbarre.set(codiceAbarre);
        }

        public int getSoglia() {
            return soglia.get();
        }

        public void setSoglia(int soglia) {
            this.soglia.set(soglia);
        }

        public double getPrezzoAcquisto() {
            return prezzoAcquisto.get();
        }

        public void setPrezzoAcquisto(double prezzoAcquisto) {
            this.prezzoAcquisto.set(prezzoAcquisto);
        }

        public double getPrezzoVendita() {
            return prezzoVendita.get();
        }

        public void setPrezzoVendita(double prezzoVendita) {
            this.prezzoVendita.set(prezzoVendita);
        }

        public byte[] getImmagine() {
            return immagine.get();
        }

        public void setImmagine(byte[] immagine) {
            this.immagine.set(immagine);
        }
    }
}