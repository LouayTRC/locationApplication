package models.Requests;

import models.Location;
import models.Reservation;
import models.User;

public class ReserveRequest {
    public String carId;
    public User user;
    public Location location;
    public boolean driver;
    public String dateStart;
    public String dateEnd;

    public ReserveRequest(String car,User user,Location loc,boolean dri,String start,String end){
        carId=car;
        this.user=user;
        location=loc;
        driver=dri;
        dateStart=start;
        dateEnd=end;
    }
}
