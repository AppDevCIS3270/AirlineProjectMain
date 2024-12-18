package org.cs3270.airlineprojectmain;

import org.cs3270.airlineprojectmain.FlightBookingController.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
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
    private TableView<BookedFlights> userTable;
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
    private TextField deleteText;
    @FXML
    private Button bookflightBt;
    @FXML
    private Button updateFlightBt;

    @FXML
    private Button manageFlightsBt;


    @FXML
    public void switchToBooking(ActionEvent event) {
        SwitchToScene.switchScene(event, "flightBooking.fxml");
    }
    @FXML
    private void signout(ActionEvent event){
        User.setUserId(-1);
        User.setIsAdmin(false); // Reset admin status
        SwitchToScene.switchScene(event,"loginScene.fxml");
}
    @FXML
    public void onDelete(){
        try {
            connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");
            String query = "DELETE FROM bookingdata WHERE flightID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(deleteText.getText()));
            int rowsAffected = preparedStatement.executeUpdate();
            updateSeatsAvailable(Integer.parseInt(deleteText.getText()));
            if(rowsAffected >0){
                System.out.println("Deleted");
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
    private void updateSeatsAvailable(int flightId) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");

            String selectSeatsQuery = "SELECT seatsAvailable FROM flightdata WHERE flightID = ?";
            PreparedStatement selectSeatsStmt = connection.prepareStatement(selectSeatsQuery);
            selectSeatsStmt.setInt(1, flightId);
            ResultSet resultSet = selectSeatsStmt.executeQuery();

            if (resultSet.next()) {
                int currentSeatsAvailable = resultSet.getInt("seatsAvailable");


                int newSeatsAvailable = currentSeatsAvailable + 1;

                String updateSeatsQuery = "UPDATE flightdata SET seatsAvailable = ? WHERE flightID = ?";
                PreparedStatement updateSeatsStmt = connection.prepareStatement(updateSeatsQuery);
                updateSeatsStmt.setInt(1, newSeatsAvailable);
                updateSeatsStmt.setInt(2, flightId);

                int rowsUpdated = updateSeatsStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Seats available updated successfully.");
                }
            } else {
                System.out.println("Flight ID not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (!User.isAdmin()) {
            manageFlightsBt.setVisible(false); // Hide "Manage Flights" button for non-admin users
        }
        bookingID.setCellValueFactory(new PropertyValueFactory<BookedFlights, Integer>("bookingID"));
        flightID.setCellValueFactory(new PropertyValueFactory<BookedFlights, Integer>("flightID"));
        departingCity.setCellValueFactory(new PropertyValueFactory<BookedFlights, String>("departingCity"));
        destinationCity.setCellValueFactory(new PropertyValueFactory<BookedFlights, String>("destinationCity"));
        flightDate.setCellValueFactory(new PropertyValueFactory<BookedFlights, LocalDateTime>("flightDate"));
        flightDate.setCellFactory(col -> {
            return new TableCell<BookedFlights, LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        // Format LocalDateTime to display as 'yyyy-MM-dd HH:mm'
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        setText(item.format(formatter));
                    } else {
                        setText(null);
                    }
                }
            };
        });
        userTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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
            ObservableList<BookedFlights> bookedData = loadBookingDataFromResultSet(resultSet);

            userTable.setItems(bookedData);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try{
                if(connection != null){
                    connection.close();
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private ObservableList<BookedFlights> loadBookingDataFromResultSet(ResultSet resultSet) {
        try{
            ObservableList<BookedFlights> bookedFlights = FXCollections.observableArrayList();

            while(resultSet.next()) {


                Integer bookingID = resultSet.getInt("bookingID");
                Integer flightID = resultSet.getInt("flightID");
                String departingCity = resultSet.getString("departingCity");
                String destinationCity = resultSet.getString("destinationCity");
                LocalDateTime flightDate = resultSet.getTimestamp("flightDate").toLocalDateTime();
                System.out.println(flightDate);

                BookedFlights bookedFlight = new BookedFlights(bookingID, flightID, departingCity, destinationCity, flightDate);
                bookedFlights.add(bookedFlight);
            }
            return bookedFlights;

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    @FXML
    public void onManageFlights(ActionEvent event){
        SwitchToScene.switchScene(event, "AdminPage.fxml");
    }
}
