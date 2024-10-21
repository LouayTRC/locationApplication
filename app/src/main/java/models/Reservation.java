package models;

import java.util.Date;

public class Reservation {
    public String _id; // Reservation ID from MongoDB
    public Client client; // Reference to the Client object
    public Car car; // Reference to the Car object
    public String locationLong; // Longitude of the reservation location
    public String locationLat; // Latitude of the reservation location
    public Double distanceEnKm;
    public Date dateStart; // Start date of the reservation
    public Date dateEnd; // End date of the reservation
    public boolean driver; // Indicates if a driver is assigned
    public Integer status; // Status of the reservation
    public Double total;
    public Integer dateDiff;// Total cost of the reservation

    // Constructor
    public Reservation(Client client, Car car, String locationLong, String locationLat, Date dateStart, Date dateEnd, boolean driver, Double total,Integer dateDiff) {
        this.client = client;
        this.car = car; // Changed to assign Car object
        this.locationLong = locationLong;
        this.locationLat = locationLat;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.driver = driver;
        this.status = 0; // Default status
        this.total = total;

    }

    @Override
    public String toString() {
        return "Reservation{" +
                "_id='" + _id + '\'' +
                ", client=" + client +
                ", car=" + car +
                ", locationLong='" + locationLong + '\'' +
                ", locationLat='" + locationLat + '\'' +
                ", dateStart=" + dateStart +
                ", dateEnd=" + dateEnd +
                ", driver=" + driver +
                ", status=" + status +
                ", total=" + total +
                ",dateDiff"+ dateDiff+
                '}';
    }
}
