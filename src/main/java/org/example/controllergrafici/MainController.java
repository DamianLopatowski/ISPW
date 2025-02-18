package org.example.controllergrafici;

import javafx.stage.Stage;
import org.example.ApplicationContext;
import org.example.controllerapplicativo.NavigationController;
import org.example.service.NavigationService;
import org.example.view.View;
import org.example.view.Login1View;
import org.example.view.Login2View;

public class MainController {
    private final View mainView;
    private final ApplicationContext context;
    private final NavigationService navigationService;
    private final boolean isOnlineMode;

    public MainController(View mainView, ApplicationContext context, boolean isOnlineMode, NavigationService navigationService) {
        this.mainView = mainView;
        this.context = context;
        this.isOnlineMode = isOnlineMode;
        this.navigationService = navigationService;

        // ✅ Imposta il NavigationService nella View
        this.mainView.setNavigationService(navigationService);

        configureMainView();
    }

    private void configureMainView() {
        mainView.getLoginButton().setOnAction(event -> {
            Stage stage = context.getStage();

            if (mainView.getInterfaccia1Option().isSelected()) {
                navigateToLogin(stage, true);
            } else if (mainView.getInterfaccia2Option().isSelected()) {
                navigateToLogin(stage, false);
            }
        });
    }

    private void navigateToLogin(Stage stage, boolean isInterfaccia1) {
        NavigationController navigationController = new NavigationController(stage);  // ✅ Creiamo il NavigationController

        if (isOnlineMode) {
            stage.setScene(new javafx.scene.Scene(new Login2View(navigationService, isOnlineMode).getRoot(), 400, 300));
            stage.setTitle("Login Online - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        } else {
            stage.setScene(new javafx.scene.Scene(new Login1View(navigationController, isOnlineMode).getRoot(), 400, 300));
            stage.setTitle("Login Offline - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        }
    }
}
