package org.cs3270.airlineprojectmain;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.cs3270.airlineprojectmain.UserClasses.User;

public class DashboardController {

    @FXML
    private Button mainMenu;
    @FXML
    private Button searchFlights;
    @FXML
    private Button myFlights;
    @FXML
    private Button logout;
    @FXML
    private Button addFlight;
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField flightID;
    @FXML
    private TextField flightDate;
    @FXML
    private TextField departingCity;
    @FXML
    private TextField destinationCity;
    @FXML
    private TextField seatsAvailable;
    @FXML
    private Button bookflightBt;

    @FXML
    public void switchToBooking(ActionEvent event) {
        SwitchToScene.switchScene(event, "flightBooking.fxml");
    }

    @FXML
    public void printUserId(){
        System.out.println(User.getUserId());
    }

    //Logic for queries, not sure how to implement yet.



}
