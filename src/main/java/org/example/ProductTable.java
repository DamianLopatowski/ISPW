package org.example;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ProductTable {

    // Costruttore privato
    private ProductTable() {}

    public static TableView<GestisciProdottiPage.Product> createProductTable(GestisciProdottiPage page) {
        TableView<GestisciProdottiPage.Product> table = new TableView<>();

        table.getColumns().addAll(
                createImageColumn(),
                createNameColumn(),
                createQuantityColumn(),
                createShelfColumn(),
                createBarcodeColumn(),
                createThresholdColumn(),
                createPurchasePriceColumn(),
                createSalePriceColumn(),
                createDeleteColumn(page),
                createEditColumn(page)
        );

        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getSelectionModel().clearSelection();

        table.setRowFactory(tv -> {
            TableRow<GestisciProdottiPage.Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> handleRowClick(event, row));
            return row;
        });

        return table;
    }

    private static TableColumn<GestisciProdottiPage.Product, ImageView> createImageColumn() {
        TableColumn<GestisciProdottiPage.Product, ImageView> column = new TableColumn<>("Immagine");
        column.setCellValueFactory(cellData -> {
            GestisciProdottiPage.Product product = cellData.getValue();
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);
            if (product.getImmagine() != null) {
                imageView.setImage(new Image(new ByteArrayInputStream(product.getImmagine())));
            }
            return new SimpleObjectProperty<>(imageView);
        });
        return column;
    }

    private static TableColumn<GestisciProdottiPage.Product, String> createNameColumn() {
        TableColumn<GestisciProdottiPage.Product, String> column = new TableColumn<>("Nome");
        column.setCellValueFactory(new PropertyValueFactory<>("nome"));
        return column;
    }

    private static TableColumn<GestisciProdottiPage.Product, Integer> createQuantityColumn() {
        TableColumn<GestisciProdottiPage.Product, Integer> column = new TableColumn<>("Quantità");
        column.setCellValueFactory(new PropertyValueFactory<>("quantita"));
        return column;
    }

    private static TableColumn<GestisciProdottiPage.Product, String> createShelfColumn() {
        TableColumn<GestisciProdottiPage.Product, String> column = new TableColumn<>("Scaffale");
        column.setCellValueFactory(new PropertyValueFactory<>("scaffale"));
        return column;
    }

    private static TableColumn<GestisciProdottiPage.Product, String> createBarcodeColumn() {
        TableColumn<GestisciProdottiPage.Product, String> column = new TableColumn<>("Codice a Barre");
        column.setCellValueFactory(new PropertyValueFactory<>("codice_a_barre"));
        return column;
    }

    private static TableColumn<GestisciProdottiPage.Product, Integer> createThresholdColumn() {
        TableColumn<GestisciProdottiPage.Product, Integer> column = new TableColumn<>("Soglia");
        column.setCellValueFactory(new PropertyValueFactory<>("soglia"));
        return column;
    }

    private static TableColumn<GestisciProdottiPage.Product, Double> createPurchasePriceColumn() {
        TableColumn<GestisciProdottiPage.Product, Double> column = new TableColumn<>("Prezzo Acquisto");
        column.setCellValueFactory(new PropertyValueFactory<>("prezzo_acquisto"));
        return column;
    }

    private static TableColumn<GestisciProdottiPage.Product, Double> createSalePriceColumn() {
        TableColumn<GestisciProdottiPage.Product, Double> column = new TableColumn<>("Prezzo Vendita");
        column.setCellValueFactory(new PropertyValueFactory<>("prezzo_vendita"));
        return column;
    }

    private static TableColumn<GestisciProdottiPage.Product, Void> createDeleteColumn(GestisciProdottiPage page) {
        TableColumn<GestisciProdottiPage.Product, Void> column = new TableColumn<>("Azioni");
        column.setCellFactory(param -> new TableCell<GestisciProdottiPage.Product, Void>() {
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
                    setOnMouseClicked(event -> handleDeleteClick(event, page));
                }
            }
        });
        return column;
    }

    private static TableColumn<GestisciProdottiPage.Product, Void> createEditColumn(GestisciProdottiPage page) {
        TableColumn<GestisciProdottiPage.Product, Void> column = new TableColumn<>("Modifica");
        column.setCellFactory(param -> new TableCell<GestisciProdottiPage.Product, Void>() {
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
                    setOnMouseClicked(event -> handleEditClick(event, page));
                }
            }
        });
        return column;
    }

    private static void handleRowClick(javafx.event.Event event, TableRow<GestisciProdottiPage.Product> row) {
        if (!row.isEmpty()) {
            if (event.getTarget() instanceof TableCell) {
                TableCell<GestisciProdottiPage.Product, ?> cell = (TableCell<GestisciProdottiPage.Product, ?>) event.getTarget();
                if (cell.getTableColumn().getText().equals("Immagine")) {
                    GestisciProdottiPage.Product product = row.getItem();
                    ImageViewWindow.openImageInNewWindow(product.getImmagine());
                }
            }
        }
    }

    private static void handleDeleteClick(javafx.event.Event event, GestisciProdottiPage page) {
        TableCell<GestisciProdottiPage.Product, Void> cell = (TableCell<GestisciProdottiPage.Product, Void>) event.getSource();
        GestisciProdottiPage.Product product = cell.getTableRow().getItem(); // Otteniamo il prodotto dalla riga
        // Finestra di conferma per la cancellazione
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Eliminazione");
        alert.setHeaderText("Sei sicuro di voler eliminare questo prodotto?");
        alert.setContentText(product.getNome());
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                page.deleteProductFromDatabase(product); // Chiamata al metodo di cancellazione
                page.refreshTable(); // Ricarichiamo la tabella dopo la cancellazione
            }
        });
    }

    private static void handleEditClick(javafx.event.Event event, GestisciProdottiPage page) {
        TableCell<GestisciProdottiPage.Product, Void> cell = (TableCell<GestisciProdottiPage.Product, Void>) event.getSource();
        GestisciProdottiPage.Product product = cell.getTableRow().getItem(); // Otteniamo il prodotto dalla riga
        page.openEditProductDialog(product); // Apriamo il dialogo di modifica
        page.refreshTable(); // Ricarichiamo la tabella dopo la modifica
    }

    // Load image from resources
    public static Image loadImage(String path) {
        InputStream inputStream = ProductTable.class.getResourceAsStream(path);
        if (inputStream != null) {
            return new Image(inputStream);
        } else {
            System.out.println("Immagine non trovata: " + path);
            return null;
        }
    }
}

