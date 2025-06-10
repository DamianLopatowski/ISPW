package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import org.example.service.ProdottoRow;

public class SogliaView {
    private final VBox root;
    private final TableView<ProdottoRow> tabella;
    private final Button indietroButton;

    public SogliaView() {
        root = new VBox(10);
        root.setPadding(new Insets(15));

        tabella = new TableView<>();

        // Colonna Nome
        TableColumn<ProdottoRow, String> nomeCol = new TableColumn<>("Nome");
        nomeCol.setCellValueFactory(data -> data.getValue().nomeProperty());

        // Colonna Quantità
        TableColumn<ProdottoRow, Integer> quantitaCol = new TableColumn<>("Quantità");
        quantitaCol.setCellValueFactory(data -> data.getValue().quantitaProperty().asObject());

        // Colonna Soglia
        TableColumn<ProdottoRow, Integer> sogliaCol = new TableColumn<>("Soglia");
        sogliaCol.setCellValueFactory(data -> data.getValue().sogliaProperty().asObject());

        // NB: la colonna "Ordinato" viene aggiunta nel controller per seguire MVC
        tabella.getColumns().addAll(nomeCol, quantitaCol, sogliaCol);

        indietroButton = new Button("Indietro");

        root.getChildren().addAll(tabella, indietroButton);
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
}
