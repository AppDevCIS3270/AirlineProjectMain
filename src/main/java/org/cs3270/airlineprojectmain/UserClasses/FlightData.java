package org.cs3270.airlineprojectmain.UserClasses;

import java.time.LocalDateTime;

public class FlightData {
    private int flightId;
    private LocalDateTime flightDate;
    private String departingCity;
    private String destinationCity;
    private int seatsAvailable;

    // Constructor: Initialize the fields with the provided values
    public FlightData(String departingCity, String destinationCity, LocalDateTime flightDate, int seatsAvailable, int flightId){
        this.departingCity = departingCity;
        this.destinationCity = destinationCity;
        this.flightDate = flightDate;
        this.seatsAvailable = seatsAvailable;
        this.flightId = flightId;
    }

    // Getters
    public int getFlightId() {
        return flightId;
    }

    public LocalDateTime getFlightDate() {
        return flightDate;
    }

    public String getDepartingCity() {
        return departingCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public int getSeatsAvailable() {
        return seatsAvailable;
    }
}