package org.cs3270.airlineprojectmain;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SceneController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onLoginPress(){
        String username = String.valueOf(usernameField);
        String password = String.valueOf(passwordField);
        System.out.println(username  + " " + password);

    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}