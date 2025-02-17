package org.example.controllergrafici;

import javafx.stage.Stage;
import org.example.ApplicationContext;
import org.example.view.View;
import org.example.view.LoginOfflineView;
import org.example.view.LoginOnlineView;

public class MainController {
    private final View mainView;
    private final ApplicationContext context;
    private boolean isOnlineMode; // Online (DB) o Offline (config.properties)

    public MainController(View mainView, ApplicationContext context, boolean isOnlineMode) {
        this.mainView = mainView;
        this.context = context;
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
            stage.setScene(new javafx.scene.Scene(new LoginOnlineView().getRoot(), 400, 300));
            stage.setTitle("Login Online - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        } else {
            stage.setScene(new javafx.scene.Scene(new LoginOfflineView().getRoot(), 400, 300));// ✅ Rimosso isInterfaccia1
            stage.setTitle("Login Offline - " + (isInterfaccia1 ? "Interfaccia 1" : "Interfaccia 2"));
        }
    }
}
