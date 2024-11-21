package org.cs3270.airlineprojectmain;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.action.Action;

import java.io.IOException;
import java.util.Objects;

public class SceneController {
    @FXML
    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

public void switchToLoginScene(ActionEvent event) throws IOException{
    try{
    root = FXMLLoader.load(getClass().getResource("loginScene.fxml"));
    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root);
    stage.setScene(scene);
    stage.show();}
    catch (Exception e){
        e.printStackTrace();
    }
}
    public void switchToSignupScene(ActionEvent event) throws IOException{
    try{
        root = FXMLLoader.load(getClass().getResource("signupScene.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();}
    catch (Exception e){
        e.printStackTrace();
     }
    }

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void onLoginPress(){
            String username = usernameField.getText();
            String password = passwordField.getText();
            System.out.println(username  + " " + password);
    }

}