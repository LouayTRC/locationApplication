package services;

import java.util.List;

import models.Driver;
import retrofit2.Call;
import retrofit2.http.GET;

public interface DriverService {

    @GET("/driver")
    Call<List<Driver>>  getAllDrivers();

}
