package org.example.controller;

import javafx.stage.Stage;
import org.example.ApplicationContext;
import org.example.view.GestioneView;

import java.util.logging.Logger;

public class GestioneController {
    private static final Logger LOGGER = Logger.getLogger(GestioneController.class.getName());
    private final GestioneView gestioneView;
    private final Stage stage;
    private final ApplicationContext context;

    public GestioneController(Stage stage, ApplicationContext context) {
        this.stage = stage;
        this.context = context;
        this.gestioneView = new GestioneView();
        setupHandlers();
    }

    private void setupHandlers() {
        gestioneView.getGestioneProdottiButton().setOnAction(event ->
                LOGGER.info("Apertura gestione prodotti...")
        );

        gestioneView.getGestioneSogliaButton().setOnAction(event ->
                LOGGER.info("Apertura gestione soglia...")
        );

        gestioneView.getGestioneSpedizioniButton().setOnAction(event ->
                LOGGER.info("Apertura gestione spedizioni...")
        );

        gestioneView.getLogoutButton().setOnAction(event -> {
            LOGGER.info("Logout effettuato. Ritorno alla schermata principale.");
            stage.getScene().setRoot(context.getMainView().getRoot());
        });
    }

    public GestioneView getGestioneView() {
        return gestioneView;
    }
}
