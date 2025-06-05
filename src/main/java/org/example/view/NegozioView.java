package org.example.view;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

public class NegozioView {
    private final BorderPane root;
    private final VBox listaProdotti;
    private final VBox carrelloBox;

    public NegozioView() {
        root = new BorderPane();
        listaProdotti = new VBox(10);
        carrelloBox = new VBox(10);

        listaProdotti.setPadding(new Insets(10));
        carrelloBox.setPadding(new Insets(10));
        carrelloBox.setStyle("-fx-background-color: #f0f0f0;");
        carrelloBox.getChildren().add(new Label("🛒 Carrello"));

        root.setLeft(listaProdotti);
        root.setRight(carrelloBox);
    }

    public BorderPane getRoot() { return root; }
    public VBox getListaProdotti() { return listaProdotti; }
    public VBox getCarrelloBox() { return carrelloBox; }
}
