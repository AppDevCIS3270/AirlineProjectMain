package org.cs3270.airlineprojectmain.UserClasses;

import java.time.LocalDateTime;

public class BookedFlights {

    private int bookingID;
    private int flightID;
    private String departingCity;
    private String destinationCity;
    private LocalDateTime flightDate;

    public BookedFlights(int bookingID, int flightID, String departingCity,String destinationCity, LocalDateTime flightDate){
        this.bookingID = bookingID;
        this.flightID = flightID;
        this.departingCity = departingCity;
        this.destinationCity = destinationCity;
        this.flightDate = flightDate;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public int getFlightID() {
        return flightID;
    }

    public void setFlightID(int flightID) {
        this.flightID = flightID;
    }

    public String getDepartingCity() {
        return departingCity;
    }

    public void setDepartingCity(String departingCity) {
        this.departingCity = departingCity;
    }

    public String getDestinationCity() {
        return destinationCity;
    }

    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }

    public LocalDateTime getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(LocalDateTime flightDate) {
        this.flightDate = flightDate;
    }
}
