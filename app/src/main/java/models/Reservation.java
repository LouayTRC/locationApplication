package models;

import java.util.Date;

public class Reservation {
    public String _id; // Reservation ID from MongoDB
    public Client client;
    public Car car;// Reference to the Client object
    public Location location; // Reference to the Car object
    public String dateStart; // Start date of the reservation
    public String dateEnd; // End date of the reservation
    public boolean driver;
    public Integer status; // Status of the reservation

    public Reservation( Client client, Location location, String dateStart, String dateEnd,boolean driver) {
        this.client = client;
        this.location = location;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.status = 0;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "client=" + client +
                ", location=" + (location != null ? location : "null") +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", driver='" + driver + '\'' +
                ", status=" + status +
                '}';
    }
}
