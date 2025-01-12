package org.example;

import javafx.stage.Stage;

public class NavigationManager {

    private static NavigationService navigationService;

    public static void initialize(Stage primaryStage) {
        if (navigationService == null) {
            navigationService = new NavigationService(primaryStage);
        }
    }

    public static NavigationService getNavigationService() {
        return navigationService;
    }
}
