package org.example;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.example.controllerapplicativo.NavigationController;
import org.example.controllerapplicativo.SessionController;
import org.example.service.NavigationService;
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(15);
        RadioButton onlineOption = new RadioButton("Modalità Online");
        RadioButton offlineOption = new RadioButton("Modalità Offline");
        ToggleGroup toggleGroup = new ToggleGroup();
        onlineOption.setToggleGroup(toggleGroup);
        offlineOption.setToggleGroup(toggleGroup);
        Button startButton = new Button("Avvia");

        startButton.setDisable(true);

        toggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) ->
                startButton.setDisable(newVal == null)
        );

        startButton.setOnAction(event -> {
            boolean isOnlineMode = onlineOption.isSelected();
            NavigationService navigationService = new NavigationController(primaryStage);
            SessionController.setIsOnlineModeStatic(isOnlineMode);
            new SessionController(primaryStage, isOnlineMode, navigationService);
        });

        root.getChildren().addAll(onlineOption, offlineOption, startButton);
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.setTitle("Seleziona la modalità");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
