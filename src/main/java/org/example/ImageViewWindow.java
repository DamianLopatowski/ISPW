package org.example;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;

public class ImageViewWindow {

    public static void openImageInNewWindow(byte[] imageBytes) {
        if (imageBytes != null) {
            Stage imageStage = new Stage();
            ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(imageBytes)));
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(600);
            imageView.setFitHeight(600);

            StackPane stackPane = new StackPane(imageView);
            Scene imageScene = new Scene(stackPane, 600, 600);
            imageStage.setTitle("Immagine ingrandita");
            imageStage.setScene(imageScene);
            imageStage.show();
        }
    }
}
