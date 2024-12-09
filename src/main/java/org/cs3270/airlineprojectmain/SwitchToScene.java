package org.cs3270.airlineprojectmain;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class SwitchToScene {

    public static void switchScene(ActionEvent event, String fxmlFilePath) {
        try {
            // Load the new scene
            Parent root = FXMLLoader.load(SwitchToScene.class.getResource(fxmlFilePath));

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
