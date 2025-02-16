package org.example.controllergrafici;

import javafx.stage.Stage;
import org.example.ApplicationContext;
import org.example.view.GestioneView;
import org.example.service.NavigationService;

import java.util.logging.Logger;

public class GestioneController {
    private static final Logger LOGGER = Logger.getLogger(GestioneController.class.getName());
    private final GestioneView gestioneView;
    private final Stage stage;
    private final ApplicationContext context;
    private final NavigationService navigationService;

    public GestioneController(Stage stage, ApplicationContext context, NavigationService navigationService) {
        this.stage = stage;
        this.context = context;
        this.navigationService = navigationService;
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
            navigationService.navigateToMainView();
        });
    }

    public GestioneView getGestioneView() {
        return gestioneView;
    }
}
