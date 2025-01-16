package org.example;

import javafx.stage.Stage;
import org.example.view.View;

public class ApplicationContext {
    private static ApplicationContext instance;

    private Stage stage;
    private View mainView;

    private ApplicationContext() {
    }

    public static ApplicationContext getInstance() {
        if (instance == null) {
            instance = new ApplicationContext();
        }
        return instance;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public View getMainView() {
        return mainView;
    }

    public void setMainView(View mainView) {
        this.mainView = mainView;
    }
}