package org.example;

import javafx.stage.Stage;
import org.example.view.View;

public class ApplicationContext {
    private final Stage stage;
    private final View mainView;

    public ApplicationContext(Stage stage, View mainView) {
        this.stage = stage;
        this.mainView = mainView;
    }

    public Stage getStage() {
        return stage;
    }
    public View getMainView() {
        return mainView;
    }
}