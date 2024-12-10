package org.cs3270.airlineprojectmain;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.cs3270.airlineprojectmain.UserClasses.FlightData;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

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
    private Button newFlight;


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
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

        // Populate the TableView with data from the database
        populateFlightsFromDatabase();

        // Set the ObservableList to the TableView
        flightsTableView.setItems(flightData);
    }

    // Move this method outside the initialize method
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
        System.out.println("Hello");
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
                /*
                Need to fix this

                 */
            } else {
                System.out.println("Failed to add flight");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
