package org.cs3270.airlineprojectmain;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertMessage {
    public static void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
