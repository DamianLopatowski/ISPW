package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class PagamentiView {
    private final VBox root;
    private final Label totaleOrdiniLabel;
    private final Label totalePagatoLabel;
    private final Label residuoLabel;
    private final TableView<OrdineTableRow> ordiniTable;
    private final Button tornaAlNegozioButton;

    public PagamentiView() {
        root = new VBox(15);
        root.setPadding(new Insets(20));

        Label title = new Label("Storico Pagamenti e Ordini");
        title.setFont(new Font(20));

        totaleOrdiniLabel = new Label();
        totalePagatoLabel = new Label();
        residuoLabel = new Label();

        VBox infoBox = new VBox(5, totaleOrdiniLabel, totalePagatoLabel, residuoLabel);
        infoBox.setPadding(new Insets(10));
        infoBox.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: gray;");

        ordiniTable = new TableView<>();
        TableColumn<OrdineTableRow, String> dataCol = new TableColumn<>("Data");
        TableColumn<OrdineTableRow, Double> importoCol = new TableColumn<>("Importo");
        TableColumn<OrdineTableRow, String> prodottiCol = new TableColumn<>("Prodotti");

        dataCol.setCellValueFactory(c -> c.getValue().dataProperty());
        importoCol.setCellValueFactory(c -> c.getValue().importoProperty().asObject());
        prodottiCol.setCellValueFactory(c -> c.getValue().prodottiProperty());

        ordiniTable.getColumns().addAll(dataCol, importoCol, prodottiCol);
        ordiniTable.setPrefHeight(300);

        tornaAlNegozioButton = new Button("🏠 Torna al Negozio");
        tornaAlNegozioButton.setPrefWidth(200);

        root.getChildren().addAll(title, infoBox, new Label("Storico Ordini:"), ordiniTable, tornaAlNegozioButton);
    }

    public VBox getRoot() {
        return root;
    }

    public Label getTotaleOrdiniLabel() {
        return totaleOrdiniLabel;
    }

    public Label getTotalePagatoLabel() {
        return totalePagatoLabel;
    }

    public Label getResiduoLabel() {
        return residuoLabel;
    }

    public TableView<OrdineTableRow> getOrdiniTable() {
        return ordiniTable;
    }

    public Button getTornaAlNegozioButton() {
        return tornaAlNegozioButton;
    }
}
