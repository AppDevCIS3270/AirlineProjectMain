package org.cs3270.airlineprojectmain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.cs3270.airlineprojectmain.UserClasses.FlightData;
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




}
