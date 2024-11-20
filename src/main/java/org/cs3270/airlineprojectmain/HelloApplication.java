package org.cs3270.airlineprojectmain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage firstStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        firstStage.setTitle("FirstStage");
        firstStage.setScene(scene);
        firstStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}