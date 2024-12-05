package org.cs3270.airlineprojectmain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import org.cs3270.airlineprojectmain.AlertMessage.*;
import java.sql.*;


public class PasswordResetController{
    @FXML
    private Button backToLogin;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField answerTextField;
    @FXML
    private Label securityQuestionLabel;
    @FXML
    private Button usernameBt;
    @FXML
    private Button answerBt;
    @FXML
    private Label enterAnswer;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField verifyPassword;
    @FXML
    private Button setPasswordBt;
    @FXML
    private Label resetSuccessful;

    Connection connection = null;
    public void onUsernameBtPress(){;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");

            String query = "SELECT security_question FROM users WHERE BINARY TRIM(username) = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, usernameField.getText());
            System.out.println(usernameField.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Executing query: " + preparedStatement.toString());

            if(resultSet.next()) {
                String securityQuestion = resultSet.getString("security_question");
                securityQuestionLabel.setText(securityQuestion);
                answerBt.setVisible(true);
                enterAnswer.setVisible(true);
                answerTextField.setVisible(true);
            }
            else{
                 AlertMessage.showAlert("No username found");
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
    public void onAnswerBtPress(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");
            String query = "SELECT security_answer FROM users WHERE BINARY TRIM(username) = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, usernameField.getText());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String securityAnswer = resultSet.getString("security_answer");
                if(securityAnswer.equalsIgnoreCase(answerTextField.getText())){
                    newPassword.setVisible(true);
                    verifyPassword.setVisible(true);
                    setPasswordBt.setVisible(true);
                }
                else {
                    AlertMessage.showAlert("ANSWER DOESNT MATCH FROM ONE IN DATABASE");
                }
            }
            else{
                AlertMessage.showAlert("ERROR WITH DATABASE");
            }

        } catch (SQLException e) {
            AlertMessage.showAlert("SOMETHING WENT WRONG" + e.getMessage());
        }
    }

    public void setPasswordBtPress(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");
            if((newPassword.getText().equals(verifyPassword.getText()))){
                System.out.println("true");
                String query = "UPDATE users SET password = ? WHERE BINARY TRIM(username) = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, newPassword.getText());
                preparedStatement.setString(2, usernameField.getText());
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected);
                resetSuccessful.setTextFill(Paint.valueOf("#07f041"));
                resetSuccessful.setText("RESET SUCCESSFUL");

            }
            else{
                AlertMessage.showAlert("Passwords do not match");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void switchToSignupScene(ActionEvent event){
        SwitchToScene.switchScene(event, "loginScene.fxml");
    }
}
