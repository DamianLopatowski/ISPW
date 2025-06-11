package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import org.example.service.ProdottoRow;

public class SogliaView {
    private final VBox root;
    private final TableView<ProdottoRow> tabella;
    private final Button indietroButton;
    private final Button vaiAGestioneProdottiButton;

    public SogliaView() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        tabella = new TableView<>();

        TableColumn<ProdottoRow, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(data -> data.getValue().nomeProperty());

        TableColumn<ProdottoRow, Integer> quantitaCol = new TableColumn<>("Quantità");
        quantitaCol.setCellValueFactory(data -> data.getValue().quantitaProperty().asObject());

        TableColumn<ProdottoRow, Integer> sogliaCol = new TableColumn<>("Soglia");
        sogliaCol.setCellValueFactory(data -> data.getValue().sogliaProperty().asObject());

        tabella.getColumns().addAll(nomeCol, quantitaCol, sogliaCol);

        indietroButton = new Button("Indietro");
        vaiAGestioneProdottiButton = new Button("Gestione Prodotti");

        HBox pulsantiBox = new HBox(10, indietroButton, vaiAGestioneProdottiButton);
        pulsantiBox.setPadding(new Insets(10, 0, 0, 0));

        root.getChildren().addAll(tabella, pulsantiBox);
    }

    public VBox getRoot() {
        return root;
    }

    public TableView<ProdottoRow> getTabella() {
        return tabella;
    }

    public Button getIndietroButton() {
        return indietroButton;
    }

    public Button getVaiAGestioneProdottiButton() {
        return vaiAGestioneProdottiButton;
    }
}
