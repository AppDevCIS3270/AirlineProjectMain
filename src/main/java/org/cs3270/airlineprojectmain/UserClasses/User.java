package org.cs3270.airlineprojectmain.UserClasses;

public class User {
    private static int userId;
    private String firstName;
    private String lastName;
    private String address;
    private String zip;
    private String state;
    private String username;
    private String password;
    private String email;
    private String ssn;
    private String securityQuestion;
    private String securityAnswer;
    boolean isAdmin;
    // Constructor
    public User(int userId, String firstName, String lastName, String address, String zip,String state, String username, String password, String email, String ssn) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.zip = zip;
        this.username = username;
        this.password = password;
    }

    // Getters for each property
    public static int getUserId() {
        return userId;
    }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getAddress() { return address; }
    public String getZip() { return zip; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }

    public static void setUserId(int userId){
        User.userId = userId;
    }


}