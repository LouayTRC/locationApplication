package models;

import java.util.List;

public class Driver {

    public String _id;
    public User user;
    public String region;
    public Double priceByDay;
    public String genre;




    @Override
    public String toString() {
        return "Driver{" +
                "_id='" + _id + '\'' +
                ", user=" + user +
                ", region='" + region + '\'' +
                ", priceByDay=" + priceByDay +
                '}';
    }
}
