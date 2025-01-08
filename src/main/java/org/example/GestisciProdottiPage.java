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
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.cell.PropertyValueFactory; // This import is necessary!

import java.sql.*;
import java.io.ByteArrayInputStream;

public class GestisciProdottiPage {

    public void start(Stage primaryStage) {
        // Crea il pulsante per tornare alla pagina principale
        Button backButton = new Button("Torna alla Pagina Prima");
        backButton.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.showMainPage(primaryStage); // Call the showMainPage method from LoginPage
        });

        // Crea la ricerca dei prodotti
        TextField searchField = new TextField();
        searchField.setPromptText("Cerca un prodotto...");

        // TableView per mostrare i prodotti (Magazzino)
        TableView<Product> magazzinoTable = createProductTable();
        // TableView per mostrare i prodotti (Negozio)
        TableView<Product> negozioTable = createProductTable();

        // Pulsante Gestione per entrare nella pagina di gestione
        Button gestioneButton = new Button("Gestione");
        gestioneButton.setOnAction(e -> showGestionePage(primaryStage));

        // Carica i prodotti in base alla modalità di accesso (online o offline)
        loadProducts(magazzinoTable, "Magazzino");
        loadProducts(negozioTable, "Negozio");

        // Imposta la ricerca dinamica dei prodotti
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchProducts(newValue, magazzinoTable, "Magazzino");
            searchProducts(newValue, negozioTable, "Negozio");
        });

        // Layout della pagina di gestione dei prodotti
        VBox vbox = new VBox(15, backButton, gestioneButton, searchField,
                new Label("Magazzino"), magazzinoTable,
                new Label("Negozio"), negozioTable);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setStyle("-fx-padding: 20;");

        Scene scene = new Scene(vbox, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TableView<Product> createProductTable() {
        TableView<Product> table = new TableView<>();

        // Colonna per l'immagine
        TableColumn<Product, ImageView> imageColumn = new TableColumn<>("Immagine");
        imageColumn.setCellValueFactory(cellData -> {
            Product product = cellData.getValue();
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50); // Dimensione iniziale piccola
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);

            if (product.getImmagine() != null) {
                imageView.setImage(new Image(new ByteArrayInputStream(product.getImmagine())));
            }

            // Aggiungiamo un listener per ingrandire l'immagine al passaggio del mouse
            imageView.setOnMouseEntered(e -> {
                imageView.setFitWidth(150);  // Dimensione grande
                imageView.setFitHeight(150);
            });

            imageView.setOnMouseExited(e -> {
                imageView.setFitWidth(50);  // Torna alla dimensione iniziale
                imageView.setFitHeight(50);
            });

            return new SimpleObjectProperty<>(imageView);
        });

        // Definiamo le altre colonne per la tabella
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

        // Aggiungi tutte le colonne alla tabella
        table.getColumns().addAll(imageColumn, nameColumn, quantityColumn, shelfColumn, barcodeColumn, thresholdColumn, purchasePriceColumn, salePriceColumn);

        return table;
    }

    private void loadProducts(TableView<Product> table, String categoria) {
        if (!LoginPage.isOffline && InternetCheck.isConnected()) {
            // Se siamo online, carichiamo i prodotti dal database
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
                                    rs.getBytes("immagine")  // Recupera l'immagine come byte[]
                            );
                            table.getItems().add(product);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Modalità offline, carichiamo solo i dati locali
            Label offlineLabel = new Label("Connessione non disponibile, i dati sono locali.");
            table.getItems().clear(); // Rimuovi i dati precedenti
            table.getItems().add(new Product("Nessun dato disponibile", 0, "-", "-", 0, 0.0, 0.0, null));
        }
    }

    private void searchProducts(String searchTerm, TableView<Product> table, String categoria) {
        table.getItems().clear();  // Pulisce la tabella
        if (!LoginPage.isOffline && InternetCheck.isConnected()) {
            // Se online, eseguiamo la ricerca nel database
            try (Connection conn = DatabaseConnection.connectToDatabase()) {
                String query = "SELECT * FROM prodotti WHERE (nome LIKE ? OR scaffale LIKE ? OR codice_a_barre LIKE ?) AND categoria = ?"; // Ricerca per nome, scaffale, o codice a barre
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    String searchPattern = "%" + searchTerm + "%"; // Aggiungi i caratteri jolly per la ricerca parziale
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
                                    rs.getBytes("immagine")  // Recupera l'immagine come byte[]
                            );
                            table.getItems().add(product);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Modalità offline, mostra solo i dati locali
            table.getItems().add(new Product("Nessun dato trovato", 0, "-", "-", 0, 0.0, 0.0, null));
        }
    }

    private void showGestionePage(Stage primaryStage) {
        GestionePage gestionePage = new GestionePage();
        gestionePage.start(primaryStage); // Start the GestionePage when the button is clicked
    }

    // Classe interna per rappresentare un prodotto
    public static class Product {
        private final SimpleStringProperty nome;
        private final SimpleIntegerProperty quantita;
        private final SimpleStringProperty scaffale;
        private final SimpleStringProperty codice_a_barre;
        private final SimpleIntegerProperty soglia;
        private final SimpleDoubleProperty prezzo_acquisto;
        private final SimpleDoubleProperty prezzo_vendita;
        private final SimpleObjectProperty<byte[]> immagine;  // Aggiungi questa proprietà per l'immagine

        public Product(String nome, int quantita, String scaffale, String codice_a_barre, int soglia, double prezzo_acquisto, double prezzo_vendita, byte[] immagine) {
            this.nome = new SimpleStringProperty(nome);
            this.quantita = new SimpleIntegerProperty(quantita);
            this.scaffale = new SimpleStringProperty(scaffale);
            this.codice_a_barre = new SimpleStringProperty(codice_a_barre);
            this.soglia = new SimpleIntegerProperty(soglia);
            this.prezzo_acquisto = new SimpleDoubleProperty(prezzo_acquisto);
            this.prezzo_vendita = new SimpleDoubleProperty(prezzo_vendita);
            this.immagine = new SimpleObjectProperty<>(immagine); // Inizializza immagine
        }

        public byte[] getImmagine() {
            return immagine.get();
        }

        public String getNome() {
            return nome.get();
        }

        public int getQuantita() {
            return quantita.get();
        }

        public String getScaffale() {
            return scaffale.get();
        }

        public String getCodice_a_barre() {
            return codice_a_barre.get();
        }

        public int getSoglia() {
            return soglia.get();
        }

        public double getPrezzo_acquisto() {
            return prezzo_acquisto.get();
        }

        public double getPrezzo_vendita() {
            return prezzo_vendita.get();
        }
    }
}
