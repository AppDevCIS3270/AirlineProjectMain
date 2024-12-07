package org.cs3270.airlineprojectmain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.cs3270.airlineprojectmain.UserClasses.FlightData;
import org.cs3270.airlineprojectmain.UserClasses.User;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class FlightBookingController implements Initializable {

    @FXML
    private TableView<FlightData> flightDataTable;
    @FXML
    private TableColumn<FlightData, String> colFrom;
    @FXML
    private TableColumn<FlightData, String> colTo;
    @FXML
    private TableColumn<FlightData, LocalDateTime> colDate;
    @FXML
    private TableColumn<FlightData, Integer> colAvailableSeats;
    @FXML
    private TableColumn<FlightData, Integer> colFlightId;
    @FXML
    private TextField flightIdText;
    @FXML
    private TextField toText;
    @FXML
    private TextField fromText;
    @FXML
    private TextField flightIdTextBox;
    @FXML
    private Button bookingBt;




    private Connection connection = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize the columns for the TableView
        colFrom.setCellValueFactory(new PropertyValueFactory<FlightData, String>("departingCity"));
        colTo.setCellValueFactory(new PropertyValueFactory<FlightData, String>("destinationCity"));
        colDate.setCellValueFactory(new PropertyValueFactory<FlightData, LocalDateTime>("flightDate"));

        colDate.setCellFactory(col -> {
            return new TableCell<FlightData, LocalDateTime>() {
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
        colAvailableSeats.setCellValueFactory(new PropertyValueFactory<FlightData, Integer>("seatsAvailable"));
        colFlightId.setCellValueFactory(new PropertyValueFactory<FlightData, Integer>("flightId"));
        flightDataTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    // Method to fetch flight data and load it into the TableView
    public void loadFlightsData() {
        try {
            // Connect to the database
            connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");
            StringBuilder query = new StringBuilder("SELECT * FROM flightdata WHERE 1=1 ");


            if(!flightIdText.getText().isEmpty()){
                query.append(" AND flightID = ?");
            }
            if(!toText.getText().isEmpty()){
                query.append(" AND destinationCity = ?");
            }
            if(!fromText.getText().isEmpty()){
                query.append(" AND departingCity = ?");
            }



            PreparedStatement preparedStatement = connection.prepareStatement(query.toString());

            int index = 1;
            if (!flightIdText.getText().isEmpty()) {
                preparedStatement.setInt(index++, Integer.parseInt(flightIdText.getText()));
            }
            if (!toText.getText().isEmpty()) {
                preparedStatement.setString(index++, toText.getText());
            }
            if (!fromText.getText().isEmpty()) {
                preparedStatement.setString(index++, fromText.getText());
            }

            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            // Load the results into an ObservableList
            ObservableList<FlightData> flightData = loadFlightDataFromResultSet(resultSet);

            // Set the ObservableList to the TableView
            flightDataTable.setItems(flightData);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Helper method to load flight data from ResultSet into ObservableList
    private ObservableList<FlightData> loadFlightDataFromResultSet(ResultSet resultSet) throws SQLException {
        ObservableList<FlightData> flightDataList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            String departingCity = resultSet.getString("DepartingCity");
            String destinationCity = resultSet.getString("DestinationCity");

            // Convert SQL DATETIME (Timestamp) to LocalDateTime
            LocalDateTime flightDate = resultSet.getTimestamp("FlightDate").toLocalDateTime();

            int seatsAvailable = resultSet.getInt("SeatsAvailable");
            int flightId = resultSet.getInt("FlightID");

            // Create a new FlightData object and add it to the list
            FlightData flight = new FlightData(departingCity, destinationCity, flightDate, seatsAvailable, flightId);
            flightDataList.add(flight);
        }
        return flightDataList;
    }



    public void onBookingBtPress() {
        // Get the flight ID from the text box
        String flightIdInput = flightIdTextBox.getText();

        // Ensure flightIdTextBox is not empty
        if (flightIdInput == null || flightIdInput.isEmpty()) {
            System.out.println("Please enter a Flight ID to book.");
            return;
        }

        try {
            // Convert the flightIdInput to an integer
            int flightId = Integer.parseInt(flightIdInput);

            // Fetch the flight data based on the flight ID
            connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");

            // Query the flight data by flight ID
            String flightQuery = "SELECT * FROM flightdata WHERE flightID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(flightQuery);
            preparedStatement.setInt(1, flightId);  // Use the flight ID from the text box

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Retrieve flight details from the result set
                String departingCity = resultSet.getString("DepartingCity");
                String destinationCity = resultSet.getString("DestinationCity");
                LocalDateTime flightDate = resultSet.getTimestamp("FlightDate").toLocalDateTime();
                int seatsAvailable = resultSet.getInt("SeatsAvailable");

                System.out.println("Flight Details: " + flightId + ", " + departingCity + ", " + destinationCity + ", " + flightDate + ", " + seatsAvailable);

                // Ensure that flightDate is not null
                if (flightDate != null) {
                    // Proceed with booking logic
                    int userId = User.getUserId();  // Assuming a method to retrieve the logged-in user ID

                    // Insert the booking record into the bookingdata table
                    String bookingQuery = "INSERT INTO bookingdata (user_id, flightID, departingCity, destinationCity, flightDate, seatsAvailable) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement bookingStmt = connection.prepareStatement(bookingQuery);
                    bookingStmt.setInt(1, userId); // user_id from logged-in user
                    bookingStmt.setInt(2, flightId); // flight_id from text box input
                    bookingStmt.setString(3, departingCity); // departing city
                    bookingStmt.setString(4, destinationCity); // destination city
                    bookingStmt.setTimestamp(5, Timestamp.valueOf(flightDate)); // flight date
                    bookingStmt.setInt(6, seatsAvailable); // seats available
                    if(ifMultipleBooked(flightId)){
                        AlertMessage.showAlert("Flight already booked on account");
                    }
                    if(seatsAvailable == 0){
                        AlertMessage.showAlert("Flight full");
                    }
                    else if(!ifMultipleBooked(flightId) && seatsAvailable > 0){
                    int rowsInserted = bookingStmt.executeUpdate();
                    if (rowsInserted > 0) {
                        System.out.println("Booking successful!");
                        // Optionally, update the available seats in the flightdata table
                        updateSeatsAvailable(flightId, seatsAvailable - 1);}
                    } else {
                        System.out.println("Booking failed.");
                    }
                } else {
                    // Handle the case where flightDate is null
                    System.out.println("Flight date is unavailable.");
                }
            } else {
                // Handle the case where no flight is found for the given ID
                System.out.println("No flight found with the given Flight ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            // Handle case when the input is not a valid integer
            System.out.println("Invalid Flight ID. Please enter a valid number.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean ifMultipleBooked(int flightId){
        try {
            String query = "SELECT COUNT(user_id) FROM bookingdata WHERE flightID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, flightId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int bookingCount = resultSet.getInt(1);
                return bookingCount > 0;
            }
            return false;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateSeatsAvailable(int flightId, int newSeatsAvailable) {
        try {
            String updateSeatsQuery = "UPDATE flightdata SET seatsAvailable = ? WHERE flightID = ?";
            PreparedStatement updateSeatsStmt = connection.prepareStatement(updateSeatsQuery);
            updateSeatsStmt.setInt(1, newSeatsAvailable); // new available seats after booking
            updateSeatsStmt.setInt(2, flightId); // flight_id of the selected flight
            updateSeatsStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}

