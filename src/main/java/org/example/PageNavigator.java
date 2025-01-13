package org.example;

import javafx.stage.Stage;

public class PageNavigator {
    private Stage stage;

    public PageNavigator(Stage stage) {
        this.stage = stage;
    }

    public void navigateTo(Page page) {
        page.start(stage);
    }
}
