package models.Requests;

import models.Location;
import models.Reservation;
import models.User;

public class ReserveRequest {
    public String carId;
    public User user;
    public Location location;
    public boolean driver;
    public Integer status;
    public String dateStart;
    public String dateEnd;

    public ReserveRequest(String car,User user,Location loc,boolean dri,Integer stat,String start,String end){
        carId=car;
        this.user=user;
        location=loc;
        driver=dri;
        status=stat;
        dateStart=start;
        dateEnd=end;
    }
}
