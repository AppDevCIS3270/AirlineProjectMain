package org.cs3270.airlineprojectmain;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
public class SignupSceneController implements Initializable {
    @FXML
    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    private Scene scene;
    @FXML
    private Parent root;


@FXML
    public void switchToLoginScene(ActionEvent event) throws IOException {
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
    // Creates a reference to the choice box in the signupScene
    @FXML
    private ChoiceBox<String> sqChoiceBox;
    // questions for the signup choicebox
    private String[] questions = {"What city were you born in?", "What is the name of your first pet", "What is your mothers maiden name?"};

    //Useful for personal project.
    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sqChoiceBox.getItems().addAll(questions);


    }
}
