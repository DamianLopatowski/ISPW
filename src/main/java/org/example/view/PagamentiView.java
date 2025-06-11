package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.example.service.OrdineTableRow;
import org.example.service.PagamentoTableRow;

public class PagamentiView {

    private final VBox root;
    private final Label totaleOrdiniLabel;
    private final Label totalePagatoLabel;
    private final Label residuoLabel;
    private final TextArea datiBonificoArea;
    private final TableView<OrdineTableRow> ordiniTable;
    private final TableView<PagamentoTableRow> pagamentiTable;
    private final Button tornaAlNegozioButton;

    public PagamentiView() {
        // Root layout
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

        datiBonificoArea = new TextArea();

        datiBonificoArea.setEditable(false);
        datiBonificoArea.setWrapText(true);
        datiBonificoArea.setStyle("-fx-control-inner-background: #f9f9f9;");
        datiBonificoArea.setPrefRowCount(5);

        ordiniTable = new TableView<>();
        ordiniTable.setPrefHeight(250);

        TableColumn<OrdineTableRow, String> dataCol = new TableColumn<>("Data");
        TableColumn<OrdineTableRow, Double> importoCol = new TableColumn<>("Importo");
        TableColumn<OrdineTableRow, String> prodottiCol = new TableColumn<>("Prodotti");

        dataCol.setCellValueFactory(c -> c.getValue().dataProperty());
        importoCol.setCellValueFactory(c -> c.getValue().importoProperty().asObject());
        prodottiCol.setCellValueFactory(c -> c.getValue().prodottiProperty());

        ordiniTable.getColumns().addAll(dataCol, importoCol, prodottiCol);

        pagamentiTable = new TableView<>();
        pagamentiTable.setPrefHeight(200);

        TableColumn<PagamentoTableRow, String> dataPagamentoCol = new TableColumn<>("Data Pagamento");
        TableColumn<PagamentoTableRow, Double> importoPagamentoCol = new TableColumn<>("Importo Pagato");

        dataPagamentoCol.setCellValueFactory(c -> c.getValue().dataProperty());
        importoPagamentoCol.setCellValueFactory(c -> c.getValue().importoProperty().asObject());

        pagamentiTable.getColumns().addAll(dataPagamentoCol, importoPagamentoCol);

        tornaAlNegozioButton = new Button("Torna al Negozio");
        tornaAlNegozioButton.setPrefWidth(200);

        root.getChildren().addAll(
                title,
                infoBox,
                datiBonificoArea,
                new Label("Storico Ordini:"),
                ordiniTable,
                new Label("Storico Pagamenti:"),
                pagamentiTable,
                tornaAlNegozioButton
        );
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

    public TextArea getDatiBonificoArea() {
        return datiBonificoArea;
    }

    public TableView<OrdineTableRow> getOrdiniTable() {
        return ordiniTable;
    }

    public TableView<PagamentoTableRow> getPagamentiTable() {
        return pagamentiTable;
    }

    public Button getTornaAlNegozioButton() {
        return tornaAlNegozioButton;
    }
}
