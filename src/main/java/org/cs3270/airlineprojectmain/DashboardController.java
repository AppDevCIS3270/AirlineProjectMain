package org.cs3270.airlineprojectmain;

import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.cs3270.airlineprojectmain.UserClasses.BookedFlights;
import org.cs3270.airlineprojectmain.UserClasses.FlightData;
import org.cs3270.airlineprojectmain.UserClasses.User;

import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private TableView<BookedFlights> bookedFlightsTableView;
    @FXML
    private TableColumn<BookedFlights, Integer> bookingID;
    @FXML
    private TableColumn<BookedFlights, Integer> flightID;
    @FXML
    private TableColumn<BookedFlights, String> departingCity;
    @FXML
    private TableColumn<BookedFlights, String> destinationCity;
    @FXML
    private TableColumn<BookedFlights, LocalDateTime> flightDate;




    @FXML
    private Button bookflightBt;
    @FXML
    private Button updateFlightBt;

    @FXML
    public void switchToBooking(ActionEvent event) {
        SwitchToScene.switchScene(event, "flightBooking.fxml");
    }
    @FXML
    private void signout(ActionEvent event){
    User.setUserId(-1);
        SwitchToScene.switchScene(event,"loginScene.fxml");
}
    @FXML
    public void printUserId(){
        System.out.println(User.getUserId());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookingID.setCellValueFactory(new PropertyValueFactory<BookedFlights, Integer>("bookingID"));
        flightID.setCellValueFactory(new PropertyValueFactory<BookedFlights, Integer>("flightID"));
        departingCity.setCellValueFactory(new PropertyValueFactory<BookedFlights, String>("departingCity"));
        destinationCity.setCellValueFactory(new PropertyValueFactory<BookedFlights, String>("destinationCity"));

        flightDate.setCellFactory(col -> {
            return new TableCell<BookedFlights, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setText(item.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                    } else {
                        setText(null);
                    }
                }
            };
        });
    }

Connection connection = null;
    @FXML
    public void onUpdateFlightBt(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");

            String query = "SELECT * FROM bookingdata WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, User.getUserId());

            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<BookedFlights> flightData = loadBookingDataFromResultSet(resultSet);

            flightData.setItems(flightData);






        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ObservableList<BookedFlights> loadBookingDataFromResultSet(ResultSet resultSet) {
        try{
            Integer bookingID = resultSet.


        }
        catch (Exception e){
            e.printStackTrace();
        }



    }

}
