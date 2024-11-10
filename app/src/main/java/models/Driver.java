package models;

import java.util.List;

public class Driver {

    public String _id;
    public User user;
    public String region;
    public Double priceByDay;

    public Driver(String _id, User user, String region, Double priceByDay) {
        this._id = _id;
        this.user = user;
        this.region = region;
        this.priceByDay = priceByDay;
    }


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
