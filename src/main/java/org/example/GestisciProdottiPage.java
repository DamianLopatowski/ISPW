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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class GestisciProdottiPage {

    // Dichiarazione di magazzinoTable e negozioTable come variabili di classe
    private TableView<Product> magazzinoTable;
    private TableView<Product> negozioTable;

    private static final String MAGAZZINO = "Magazzino";

    public void start(Stage primaryStage) {
        Button backButton = new Button("Torna alla Pagina Prima");
        backButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.showMainPage(primaryStage);
        });

        TextField searchField = new TextField();
        searchField.setPromptText("Cerca un prodotto...");

        // Inizializzazione delle tabelle
        magazzinoTable = createProductTable();
        negozioTable = createProductTable();

        Button gestioneButton = new Button("Gestione");
        gestioneButton.setOnAction(e -> showGestionePage(primaryStage));

        loadProducts(magazzinoTable, MAGAZZINO);
        loadProducts(negozioTable, "Negozio");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchProducts(newValue, magazzinoTable, MAGAZZINO);
            searchProducts(newValue, negozioTable, "Negozio");
        });

        VBox vbox = new VBox(15, backButton, gestioneButton, searchField,
                new Label(MAGAZZINO), magazzinoTable,
                new Label("Negozio"), negozioTable);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(vbox, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<Product> createProductTable() {
        TableView<Product> table = new TableView<>();

        TableColumn<Product, ImageView> imageColumn = new TableColumn<>("Immagine");
        imageColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);

            if (product.getImmagine() != null) {
                imageView.setImage(new Image(new ByteArrayInputStream(product.getImmagine())));
            }

            return new SimpleObjectProperty<>(imageView);
        });

        TableColumn<Product, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantità");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantita"));

        TableColumn<Product, String> shelfColumn = new TableColumn<>("Scaffale");
        shelfColumn.setCellValueFactory(new PropertyValueFactory<>("scaffale"));

        TableColumn<Product, String> barcodeColumn = new TableColumn<>("Codice a Barre");
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("codice_a_barre"));

        TableColumn<Product, Integer> thresholdColumn = new TableColumn<>("Soglia");
        thresholdColumn.setCellValueFactory(new PropertyValueFactory<>("soglia"));

        TableColumn<Product, Double> purchasePriceColumn = new TableColumn<>("Prezzo Acquisto");
        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>("prezzo_acquisto"));

        TableColumn<Product, Double> salePriceColumn = new TableColumn<>("Prezzo Vendita");
        salePriceColumn.setCellValueFactory(new PropertyValueFactory<>("prezzo_vendita"));

        // Icona del secchio per eliminare il prodotto
        TableColumn<Product, Void> deleteColumn = new TableColumn<>("Azioni");
        deleteColumn.setCellFactory(param -> new TableCell<Product, Void>() {
            private final ImageView trashIcon = new ImageView(loadImage("/icons/trash-icon.png"));

            {
                trashIcon.setFitWidth(20);
                trashIcon.setFitHeight(20);
                trashIcon.setPreserveRatio(true);
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(trashIcon);
                    setOnMouseClicked(event -> {
                        Product product = getTableView().getItems().get(getIndex());
                        // Finestra di conferma per l'eliminazione
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Conferma Eliminazione");
                        alert.setHeaderText("Sei sicuro di voler eliminare questo prodotto?");
                        alert.setContentText(product.getNome());
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                deleteProductFromDatabase(product);
                                refreshTable();  // Ricarica i dati dopo eliminazione
                            }
                        });
                    });
                }
            }
        });

        // Icona della penna per modificare il prodotto
        TableColumn<Product, Void> editColumn = new TableColumn<>("Modifica");
        editColumn.setCellFactory(param -> new TableCell<Product, Void>() {
            private final ImageView penIcon = new ImageView(loadImage("/icons/pen-icon.png"));

            {
                penIcon.setFitWidth(20);
                penIcon.setFitHeight(20);
                penIcon.setPreserveRatio(true);
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(penIcon);
                    setOnMouseClicked(event -> {
                        Product product = getTableView().getItems().get(getIndex());
                        // Apertura della finestra di modifica del prodotto
                        openEditProductDialog(product);
                        refreshTable();  // Ricarica i dati dopo modifica
                    });
                }
            }
        });

        table.getColumns().addAll(imageColumn, nameColumn, quantityColumn, shelfColumn, barcodeColumn, thresholdColumn, purchasePriceColumn, salePriceColumn, deleteColumn, editColumn);

        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getSelectionModel().clearSelection();

        // Gestiamo il clic sulla riga per aprire l'immagine solo se è stata cliccata la cella contenente l'immagine
        table.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                // Se il clic è stato fatto sulla cella dell'immagine (e non sulle icone di modifica o eliminazione)
                if (!row.isEmpty()) {
                    // Verifica se il clic non è stato fatto su un'icona di modifica o eliminazione
                    if (event.getTarget() instanceof TableCell) {
                        TableCell<Product, ?> cell = (TableCell<Product, ?>) event.getTarget();
                        if (cell.getTableColumn().getText().equals("Immagine")) {
                            // Cliccato sulla cella dell'immagine, apri l'immagine
                            Product product = row.getItem();
                            ImageViewWindow.openImageInNewWindow(product.getImmagine());
                        }
                    }
                }
            });
            return row;
        });

        return table;
    }

    private Image loadImage(String path) {
        InputStream inputStream = getClass().getResourceAsStream(path);
        if (inputStream != null) {
            return new Image(inputStream);
        } else {
            System.out.println("Immagine non trovata: " + path);
            return null;
        }
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
                                    rs.getInt("quantita"),
                                    rs.getString("scaffale"),
                                    rs.getString("codice_a_barre"),
                                    rs.getInt("soglia"),
                                    rs.getDouble("prezzo_acquisto"),
                                    rs.getDouble("prezzo_vendita"),
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
                                    rs.getInt("quantita"),
                                    rs.getString("scaffale"),
                                    rs.getString("codice_a_barre"),
                                    rs.getInt("soglia"),
                                    rs.getDouble("prezzo_acquisto"),
                                    rs.getDouble("prezzo_vendita"),
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
    private void deleteProductFromDatabase(Product product) {
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
    private void openEditProductDialog(Product product) {
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
    private void refreshTable() {
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
