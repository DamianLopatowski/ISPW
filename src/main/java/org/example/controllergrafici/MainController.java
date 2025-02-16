package org.example.controllergrafici;

import javafx.stage.Stage;
import org.example.ApplicationContext;
import org.example.NetworkUtils;
import org.example.view.View;
import org.example.service.NavigationService;

import java.util.logging.Logger;

public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());

    private final View mainView;
    private final ApplicationContext context;
    private final NavigationService navigationService;

    public MainController(View mainView, ApplicationContext context, NavigationService navigationService) {
        this.mainView = mainView;
        this.context = context;
        this.navigationService = navigationService;
        configureMainView();
    }

    private void configureMainView() {
        mainView.getLoginPersonaleButton().setOnAction(event -> {
            Stage stage = context.getStage(); // Recupera Stage dal contesto dell'applicazione
            new LoginPersonalController(stage, mainView, navigationService);
        });
    }
}
