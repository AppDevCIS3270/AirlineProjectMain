package org.cs3270.airlineprojectmain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
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

    @FXML
    private Button dashboardBt;
    @FXML
    private Button ResetBt;
    @FXML
    private Label ErrorText;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    Connection connection = null;
    @FXML
    public void switchToSignupScene(ActionEvent event){
        SwitchToScene.switchScene(event, "signupScene.fxml");
    }
    @FXML
    public void switchToResetScene(ActionEvent event) {
        SwitchToScene.switchScene(event, "passwordReset.fxml");
    }

    @FXML
    public void switchToDashboardScene(ActionEvent event){
        SwitchToScene.switchScene(event, "Dashboard.fxml");
    }



    @FXML
    private void onLoginPress(){
        try{
            System.out.println(usernameField.getText());
            System.out.println(passwordField.getText());

            // attempts to make a connection to our database
            connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");

            // query to match username and password
            String query = "SELECT * FROM users WHERE BINARY username = ? AND  BINARY password = ?";

            // uses a prepared statement so we can implement our own username and passwords
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // grabs the entered username and password from the user
            preparedStatement.setString(1, usernameField.getText());
            preparedStatement.setString(2, passwordField.getText());

            // runs the query from the prepared statement and the loginScene fields
            ResultSet resultSet = preparedStatement.executeQuery();

            // checks to see if username and password are available in the table
            if(resultSet.next()){
                ErrorText.setTextFill(Paint.valueOf("#07f041"));
                ErrorText.setText("Login Successful");
                System.out.println("Login Successful " + resultSet.getString("user_id"));

                dashboardBt.setVisible(true);

            }
            else{
                ErrorText.setTextFill(Paint.valueOf("#f00707"));
                ErrorText.setText("invalid username or password");
                System.out.println("Invalid username or password");
            }
        }
        catch (Exception e){
            // prints whatever errors pop up during run time
            e.printStackTrace();
        }
        finally {
            if(connection != null){
                try{
                    connection.close();
                }
                catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
