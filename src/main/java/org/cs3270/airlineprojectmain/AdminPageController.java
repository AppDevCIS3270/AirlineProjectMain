package org.cs3270.airlineprojectmain;

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

import static org.cs3270.airlineprojectmain.AlertMessage.showAlert;

public class AdminPageController implements Initializable {
    @FXML
    private TextField flightDate;
    @FXML
    private TextField departingCity;
    @FXML
    private TextField destinationCity;
    @FXML
    private TextField availableSeats;
    @FXML
    private TextField deleteTextID;
    @FXML
    private TextField updateTextID;

    @FXML
    private Button newFlight;
    @FXML
    private Button deleteFlightBt;
    @FXML
    private Button backToDB;
    @FXML
    private Button updateFlightBt;


/**
    private boolean isAdmin;

     * Sets whether the current user is an admin and configures the UI.

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        configureUIForRole();
    }


    /**
     * Adjusts UI elements based on the user's role.

    private void configureUIForRole() {
        if (!isAdmin) {
            // Hide admin-specific buttons
            newFlight.setVisible(false);
            newFlight.setManaged(false); // Prevents it from occupying space
            deleteFlightBt.setVisible(false);
            deleteFlightBt.setManaged(false);
        }
    }
    */


    @FXML
    private TableView<FlightData> flightsTableView;
    @FXML
    private TableColumn<FlightData, String> flightIdColumn;
    @FXML
    private TableColumn<FlightData, String> flightDateColumn;
    @FXML
    private TableColumn<FlightData, String> departingCityColumn;
    @FXML
    private TableColumn<FlightData, String> destinationCityColumn;
    @FXML
    private TableColumn<FlightData, String> availableSeatsColumn;

    private ObservableList<FlightData> flightData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the list
        flightData = FXCollections.observableArrayList();

        // Set up the columns to display data
        flightIdColumn.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        departingCityColumn.setCellValueFactory(new PropertyValueFactory<>("departingCity"));
        destinationCityColumn.setCellValueFactory(new PropertyValueFactory<>("destinationCity"));
        flightDateColumn.setCellValueFactory(new PropertyValueFactory<>("flightDate"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("seatsAvailable"));

        // Populate the TableView with data from the database
        populateFlightsFromDatabase();

        // Set the ObservableList to the TableView
        flightsTableView.setItems(flightData);
    }

    public void back(ActionEvent event) {
        SwitchToScene.switchScene(event, "Dashboard.fxml");
    }

    // Method to populate the TableView with data from the database
    private void populateFlightsFromDatabase() {
        Connection connection = null;

        try {
            // Connect to the database
            connection = DriverManager.getConnection(
                    "jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database",
                    "username",
                    "Password!"
            );

            // Execute a query to fetch flight data
            String query = "SELECT flightID, flightDate, departingCity, destinationCity, seatsAvailable FROM flightdata";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set and add data to the ObservableList
            while (resultSet.next()) {
                int flightId = resultSet.getInt("flightID");
                LocalDateTime flightDate = resultSet.getTimestamp("flightDate").toLocalDateTime();
                String departingCity = resultSet.getString("departingCity");
                String destinationCity = resultSet.getString("destinationCity");
                int seatsAvailable = resultSet.getInt("seatsAvailable");

                // Create a new FlightData object with the necessary parameters
                FlightData flight = new FlightData(departingCity, destinationCity, flightDate, seatsAvailable, flightId);

                // Add the new flight to the ObservableList
                flightData.add(flight);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close the connection
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void createNewFlight(ActionEvent event) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!")) {
            String query = "INSERT INTO flightdata (flightDate, departingCity, destinationCity, seatsAvailable) VALUES (?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Convert flightDate to LocalDateTime and set it to the preparedStatement
            preparedStatement.setTimestamp(1, Timestamp.valueOf(flightDate.getText()));
            preparedStatement.setString(2, departingCity.getText());
            preparedStatement.setString(3, destinationCity.getText());
            preparedStatement.setInt(4, Integer.parseInt(availableSeats.getText()));

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Flight added");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Flight Created Successfully");
                alert.setHeaderText(null);
                alert.setContentText("The new Flight has been added to the Database");
                alert.showAndWait();
            } else {
                System.out.println("Failed to add flight");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void ondeleteFlight(ActionEvent event) throws SQLException {
        Connection connection = null;
        try {
            // Connect to the database
            connection = DriverManager.getConnection(
                    "jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database",
                    "username",
                    "Password!"
            );

            // Delete the flight by flightID
            String deleteFlightQuery = "DELETE FROM flightData WHERE flightID = ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteFlightQuery);
            int flightIdToDelete = Integer.parseInt(deleteTextID.getText());
            deleteStatement.setInt(1, flightIdToDelete);
            // Delete from booked flights as well
            String deleteBookingsQuery = "DELETE FROM bookingData WHERE flightID = ?";
            PreparedStatement deleteBookingsStatement = connection.prepareStatement(deleteBookingsQuery);
            deleteBookingsStatement.setInt(1, flightIdToDelete);
            deleteBookingsStatement.executeUpdate();
            int rowsAffected = deleteStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Flight Deleted");

                // Now reassign sequential flight IDs
                String resetIdsQuery = "SET @count = 0;";
                Statement resetIdsStatement = connection.createStatement();
                resetIdsStatement.executeUpdate(resetIdsQuery); // Execute the SET statement

                String updateQuery = "UPDATE flightdata SET flightID = (@count := @count + 1) ORDER BY flightID";
                Statement updateStmt = connection.createStatement();
                updateStmt.executeUpdate(updateQuery); // Execute the UPDATE statement

                // Refresh the ObservableList and TableView
                flightData.clear();
                populateFlightsFromDatabase();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Flight Deleted Successfully");
                alert.setHeaderText(null);
                alert.setContentText("The flight has been removed, and IDs have been updated.");
                alert.showAndWait();
            } else {
                System.out.println("Failed to remove flight");
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

    @FXML
    public void onUpdateFlightBt() {
        String updateQuery = "UPDATE flightdata SET flightDate = ?, departingCity = ?, destinationCity = ?, seatsAvailable = ? WHERE flightID = ?";

        try (Connection connection = DriverManager.getConnection(
                "jdbc:mysql://cis3270airlinedatabase.mysql.database.azure.com/database", "username", "Password!");
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            // Get inputs
            int flightIdToUpdate = Integer.parseInt(updateTextID.getText());
            LocalDateTime parsedDate = LocalDateTime.parse(flightDate.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String updatedDepartingCity = departingCity.getText();
            String updatedDestinationCity = destinationCity.getText();
            int updatedSeatsAvailable = Integer.parseInt(availableSeats.getText());

            // Set parameters for the query
            preparedStatement.setTimestamp(1, Timestamp.valueOf(parsedDate));
            preparedStatement.setString(2, updatedDepartingCity);
            preparedStatement.setString(3, updatedDestinationCity);
            preparedStatement.setInt(4, updatedSeatsAvailable);
            preparedStatement.setInt(5, flightIdToUpdate);

            // Execute the update
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Flight Updated");
                alert.setHeaderText(null);
                alert.setContentText("Flight details updated successfully!");
                alert.showAndWait();;
                populateFlightsFromDatabase(); // Refresh the TableView
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("No flight found with the provided ID!");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while updating the flight.");
            alert.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Error");
            alert.setHeaderText(null);
            alert.setContentText( "Please check your inputs and try again.");
            alert.showAndWait();
        }
        // Refresh the ObservableList and TableView
        flightData.clear();
        populateFlightsFromDatabase();
    }
}
