package models;

import java.util.Date;

public class Reservation {
    public String _id; // Reservation ID from MongoDB
    public Client client; // Reference to the Client object
    public Car car; // Reference to the Car object
    public Date dateStart; // Start date of the reservation
    public Date dateEnd; // End date of the reservation
    public Integer status; // Status of the reservation

    public Reservation(String _id, Client client, Car car, Date dateStart, Date dateEnd, Integer status) {
        this._id = _id;
        this.client = client;
        this.car = car;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.status = status;
    }

}
