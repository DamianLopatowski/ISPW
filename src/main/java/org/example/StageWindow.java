package org.example;

import javafx.stage.Stage;

public class StageWindow implements Window {
    private Stage stage;

    public StageWindow(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void showPage(Page page) {
        page.start(stage);
    }
}
