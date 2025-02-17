package org.example.controllergrafici;

import javafx.stage.Stage;
import org.example.ApplicationContext;
import org.example.view.View;
import org.example.service.NavigationService;
import org.example.dao.GestoreDAOImpl;
import org.example.view.LoginOfflineView;
import org.example.view.LoginOnlineView;

public class MainController {
    private final View mainView;
    private final ApplicationContext context;
    private final NavigationService navigationService;
    private boolean isOnlineMode; // Online (DB) o Offline (config.properties)

    public MainController(View mainView, ApplicationContext context, NavigationService navigationService, boolean isOnlineMode) {
        this.mainView = mainView;
        this.context = context;
        this.navigationService = navigationService;
        this.isOnlineMode = isOnlineMode;
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
        if (isOnlineMode) {
            stage.setScene(new javafx.scene.Scene(new LoginOnlineView(isInterfaccia1).getRoot(), 400, 300));
            stage.setTitle("Login Online - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        } else {
            stage.setScene(new javafx.scene.Scene(new LoginOfflineView(isInterfaccia1).getRoot(), 400, 300));
            stage.setTitle("Login Offline - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        }
    }
}
