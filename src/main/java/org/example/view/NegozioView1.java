package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class NegozioView1 {
    private final BorderPane root;
    private final ScrollPane scrollPaneProdotti;
    private final FlowPane flowPaneProdotti;
    private final VBox carrelloBox;
    private final Button inviaOrdineButton = new Button("Invia Ordine");
    private final Button logoutButton = new Button("Logout");
    private final Button profiloButton = new Button("Modifica Profilo");


    public NegozioView1() {
        root = new BorderPane();

        // FlowPane responsivo
        flowPaneProdotti = new FlowPane();
        flowPaneProdotti.setHgap(20);
        flowPaneProdotti.setVgap(20);
        flowPaneProdotti.setPadding(new Insets(10));
        flowPaneProdotti.setPrefWrapLength(0); // si adatta automaticamente

        scrollPaneProdotti = new ScrollPane(flowPaneProdotti);
        scrollPaneProdotti.setFitToWidth(true);
        scrollPaneProdotti.setStyle("-fx-background: white;");
        scrollPaneProdotti.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneProdotti.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        carrelloBox = new VBox(10);
        carrelloBox.setPadding(new Insets(10));
        carrelloBox.setPrefWidth(250);
        carrelloBox.setStyle("-fx-background-color: #f0f0f0;");
        carrelloBox.getChildren().addAll(new Label("🛒 Carrello"), inviaOrdineButton, logoutButton, profiloButton);

        root.setCenter(scrollPaneProdotti);
        root.setRight(carrelloBox);

    }

    public BorderPane getRoot() { return root; }
    public FlowPane getFlowPaneProdotti() { return flowPaneProdotti; }
    public VBox getCarrelloBox() { return carrelloBox; }
    public Button getInviaOrdineButton() { return inviaOrdineButton; }
    public Button getLogoutButton() { return logoutButton; }
    public Button getProfiloButton() {
        return profiloButton;
    }
}
