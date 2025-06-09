package org.example.view;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class NegozioView1 {
    private final BorderPane root;
    private final ScrollPane scrollPaneProdotti;
    private final FlowPane flowPaneProdotti;
    private final VBox carrelloBox;
    private final VBox righeCarrelloBox; // Contenitore dinamico per righe carrello
    private final Label totaleLabel;     //Totale ordine
    private final Button inviaOrdineButton = new Button("Invia Ordine");
    private final Button logoutButton = new Button("Logout");
    private final Button profiloButton = new Button("Modifica Profilo");
    private final Button visualizzaPagamentiButton = new Button("Pagamenti & Ordini");

    public NegozioView1() {
        root = new BorderPane();

        // Area prodotti centrale (scrollabile)
        flowPaneProdotti = new FlowPane();
        flowPaneProdotti.setHgap(20);
        flowPaneProdotti.setVgap(20);
        flowPaneProdotti.setPadding(new Insets(10));
        flowPaneProdotti.setPrefWrapLength(0); // Si adatta automaticamente

        scrollPaneProdotti = new ScrollPane(flowPaneProdotti);
        scrollPaneProdotti.setFitToWidth(true);
        scrollPaneProdotti.setStyle("-fx-background: white;");
        scrollPaneProdotti.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneProdotti.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        //Carrello (VBox interna)
        carrelloBox = new VBox(10);
        carrelloBox.setPadding(new Insets(10));
        carrelloBox.setPrefWidth(270); // Larghezza adeguata per contenuti
        carrelloBox.setStyle("""
            -fx-background-color: #f0f0f0;
            -fx-padding: 15;
            -fx-font-size: 13px;
            -fx-border-color: #cccccc;
            -fx-border-width: 1px;
        """);

        Label titoloCarrello = new Label("Carrello");
        titoloCarrello.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        righeCarrelloBox = new VBox(5); // Contenuto dinamico del carrello

        totaleLabel = new Label("Totale: €0.00"); // Totale aggiornato dal controller
        totaleLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5 0 0 0;");

        // Pulsanti in fondo
        carrelloBox.getChildren().addAll(
                titoloCarrello,
                righeCarrelloBox,
                totaleLabel,
                inviaOrdineButton,
                logoutButton,
                profiloButton,
                visualizzaPagamentiButton
        );

        // ScrollPane esterno al carrello
        ScrollPane scrollCarrello = new ScrollPane(carrelloBox);
        scrollCarrello.setFitToWidth(true);
        scrollCarrello.setStyle("-fx-background-color: transparent;");
        scrollCarrello.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollCarrello.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollCarrello.setPrefWidth(300);
        scrollCarrello.setMinWidth(280);

        // Layout principale
        root.setCenter(scrollPaneProdotti);
        root.setRight(scrollCarrello);
    }

    public BorderPane getRoot() {
        return root;
    }

    public FlowPane getFlowPaneProdotti() {
        return flowPaneProdotti;
    }

    public VBox getCarrelloBox() {
        return carrelloBox;
    }

    public VBox getRigheCarrelloBox() {
        return righeCarrelloBox;
    }

    public Label getTotaleLabel() {
        return totaleLabel;
    }

    public Button getInviaOrdineButton() {
        return inviaOrdineButton;
    }

    public Button getLogoutButton() {
        return logoutButton;
    }

    public Button getProfiloButton() {
        return profiloButton;
    }
    public Button getVisualizzaPagamentiButton() {
        return visualizzaPagamentiButton;
    }
}
