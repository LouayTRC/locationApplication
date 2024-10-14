package models.Requests;

import models.Location;
import models.Reservation;

public class ReserveRequest {
    public String carId;
    public String userId;
    public Location location;
    public boolean driver;
    public Integer status;
    public String dateStart;
    public String dateEnd;

    public ReserveRequest(String car,String user,Location loc,boolean dri,Integer stat,String start,String end){
        carId=car;
        userId=user;
        location=loc;
        driver=dri;
        status=stat;
        dateStart=start;
        dateEnd=end;
    }
}
