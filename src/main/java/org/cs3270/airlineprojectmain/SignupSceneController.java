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
import org.w3c.dom.Text;

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
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField lastnameField;
    @FXML
    private TextField adressField;
    @FXML
    private TextField zipField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ssnField;
    @FXML
    private TextField sqAnswerField;




@FXML
Connection connection = null;
    @FXML
                //NEED TO MAKE SURE THAT USERS CANNOT HAVE DUPLICATE ACCOUNTS, USE SSN AS A WAY TO MAKE SURE, FIGURE THIS OUT, AND MAKE SURE TO USE GENERIC TO FORMAT SSN AND EMAILS SO IT HAS VALID VALUES. THANK YOU FUTURE AJDIN
    public void onSignupPress(){
        try{
            String selectedQuestion = sqChoiceBox.getValue();
            System.out.println(firstnameField.getText());
            System.out.println(lastnameField.getText());
            System.out.println(adressField.getText());
            System.out.println(zipField.getText());
            System.out.println(stateField.getText());
            System.out.println(usernameField.getText());
            System.out.println(passwordField.getText());
            System.out.println(emailField.getText());
            System.out.println(ssnField.getText());
            System.out.println(selectedQuestion);
            System.out.println(sqAnswerField.getText());


           connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");

          String query = "INSERT INTO users (first_name, last_name, address, zip, state, username, password, email, ssn, security_question, security_answer) VALUES (BINARY ?,BINARY ?,BINARY ?,BINARY ?,BINARY ?,BINARY ?,BINARY ?,BINARY ?,BINARY ?,BINARY ?,BINARY ?)";
          PreparedStatement preparedStatement = connection.prepareStatement(query);
          preparedStatement.setString(1,firstnameField.getText() );
          preparedStatement.setString(2,lastnameField.getText() );
          preparedStatement.setString(3,adressField.getText() );
          preparedStatement.setString(4,zipField.getText() );
          preparedStatement.setString(5,stateField.getText() );
          preparedStatement.setString(6, usernameField.getText() );
          preparedStatement.setString(7,passwordField.getText() );
          preparedStatement.setString(8,emailField.getText() );
          preparedStatement.setString(9,ssnField.getText() );
          preparedStatement.setString(10,selectedQuestion );
          preparedStatement.setString(11,sqAnswerField.getText() );

          int rowsInserted = preparedStatement.executeUpdate();

          if(rowsInserted > 0){
              System.out.println("User registered successfuly.");
          }
          else{
              System.out.println("Failed to register user.");
          }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
