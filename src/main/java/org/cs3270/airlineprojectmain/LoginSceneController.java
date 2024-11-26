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
public class LoginSceneController {
    @FXML
    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }
    @FXML
    private Scene scene;
    @FXML
    private Parent root;
// Switches the scene to the LoginScene
// Switches the scene to the SignupScene
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
        try{
            System.out.println(usernameField.getText());
            System.out.println(passwordField.getText());

// attempts to make a connection to our database
            Connection connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");
            // query to match username and password
            String query = "SELECT * FROM users WHERE BINARY username = ? AND  BINARY password = ?";
// uses a prepared statemnt so we can impliment our own username and passwords
            PreparedStatement preparedStatement = connection.prepareStatement(query);
// grabs the entered username and password from the user
            preparedStatement.setString(1, usernameField.getText());
            preparedStatement.setString(2, passwordField.getText());
// runs the query from the prepared statement and the loginScene fields
            ResultSet resultSet = preparedStatement.executeQuery();
            // checks to see if username and password are available in the table
            if(resultSet.next()){
                System.out.println("Login Succesful " + resultSet.getString("user_id"));
            }
            else{
                System.out.println("Invalid username or password");
            }
            // closes the connection
            connection.close();
        }
        catch (Exception e){
            // prints whatever errors pop up during run time
            e.printStackTrace();
        }
    }


}
