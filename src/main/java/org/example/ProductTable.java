package org.example;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ProductTable {

    public static TableView<GestisciProdottiPage.Product> createProductTable(GestisciProdottiPage page) {
        TableView<GestisciProdottiPage.Product> table = new TableView<>();

        // Access constants from GestisciProdottiPage
        String QUANTITA = "quantita";
        String SCAFFALE = "scaffale";
        String CODICE_A_BARRE = "codice_a_barre";
        String SOGLIA = "soglia";
        String PREZZO_ACQUISTO = "prezzo_acquisto";
        String PREZZO_VENDITA = "prezzo_vendita";

        // Column for image
        TableColumn<GestisciProdottiPage.Product, ImageView> imageColumn = new TableColumn<>("Immagine");
        imageColumn.setCellValueFactory(cellData -> {
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

        // Column for product name
        TableColumn<GestisciProdottiPage.Product, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nome"));

        // Column for quantity
        TableColumn<GestisciProdottiPage.Product, Integer> quantityColumn = new TableColumn<>("Quantità");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>(QUANTITA));

        // Column for shelf
        TableColumn<GestisciProdottiPage.Product, String> shelfColumn = new TableColumn<>("Scaffale");
        shelfColumn.setCellValueFactory(new PropertyValueFactory<>(SCAFFALE));

        // Column for barcode
        TableColumn<GestisciProdottiPage.Product, String> barcodeColumn = new TableColumn<>("Codice a Barre");
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>(CODICE_A_BARRE));

        // Column for threshold
        TableColumn<GestisciProdottiPage.Product, Integer> thresholdColumn = new TableColumn<>("Soglia");
        thresholdColumn.setCellValueFactory(new PropertyValueFactory<>(SOGLIA));

        // Column for purchase price
        TableColumn<GestisciProdottiPage.Product, Double> purchasePriceColumn = new TableColumn<>("Prezzo Acquisto");
        purchasePriceColumn.setCellValueFactory(new PropertyValueFactory<>(PREZZO_ACQUISTO));

        // Column for sale price
        TableColumn<GestisciProdottiPage.Product, Double> salePriceColumn = new TableColumn<>("Prezzo Vendita");
        salePriceColumn.setCellValueFactory(new PropertyValueFactory<>(PREZZO_VENDITA));

        // Icon for delete action
        TableColumn<GestisciProdottiPage.Product, Void> deleteColumn = new TableColumn<>("Azioni");
        deleteColumn.setCellFactory(param -> new TableCell<GestisciProdottiPage.Product, Void>() {
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
                        GestisciProdottiPage.Product product = getTableView().getItems().get(getIndex());
                        // Confirmation window for deletion
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Conferma Eliminazione");
                        alert.setHeaderText("Sei sicuro di voler eliminare questo prodotto?");
                        alert.setContentText(product.getNome());
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                page.deleteProductFromDatabase(product);  // Call delete method from GestisciProdottiPage
                                page.refreshTable();  // Reload the table after deletion
                            }
                        });
                    });
                }
            }
        });

        // Icon for edit action
        TableColumn<GestisciProdottiPage.Product, Void> editColumn = new TableColumn<>("Modifica");
        editColumn.setCellFactory(param -> new TableCell<GestisciProdottiPage.Product, Void>() {
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
                        GestisciProdottiPage.Product product = getTableView().getItems().get(getIndex());
                        // Open edit dialog for the product
                        page.openEditProductDialog(product);
                        page.refreshTable();  // Reload the table after editing
                    });
                }
            }
        });

        table.getColumns().addAll(imageColumn, nameColumn, quantityColumn, shelfColumn, barcodeColumn, thresholdColumn, purchasePriceColumn, salePriceColumn, deleteColumn, editColumn);

        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getSelectionModel().clearSelection();

        // Handle row click to open image when clicking the image cell
        table.setRowFactory(tv -> {
            TableRow<GestisciProdottiPage.Product> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                // If the click is on an image cell, open the image
                if (!row.isEmpty()) {
                    if (event.getTarget() instanceof TableCell) {
                        TableCell<GestisciProdottiPage.Product, ?> cell = (TableCell<GestisciProdottiPage.Product, ?>) event.getTarget();
                        if (cell.getTableColumn().getText().equals("Immagine")) {
                            GestisciProdottiPage.Product product = row.getItem();
                            ImageViewWindow.openImageInNewWindow(product.getImmagine());
                        }
                    }
                }
            });
            return row;
        });

        return table;
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
