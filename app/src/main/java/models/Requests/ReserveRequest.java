package models.Requests;

import models.Driver;
import models.Location;

public class ReserveRequest {
    public String carId;
    public String user;
    public Location location;
    public String driver;
    public String dateStart;
    public String dateEnd;

    public ReserveRequest(String car, String user, String start, String end,Location loc, String dri) {
        carId = car;
        this.user = user;
        location = loc;
        driver = dri;
        dateStart = start;
        dateEnd = end;
    }

    @Override
    public String toString() {
        return "ReserveRequest{" +
                "carId='" + carId + '\'' +
                ", user='" + user + '\'' +
                ", location=" + (location != null ? location.toString() : "null") +
                ", driver=" + driver +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                '}';
    }
}
