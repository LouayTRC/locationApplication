package services;

import java.util.List;

import models.Driver;
import models.Requests.AvailabilityRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DriverService {

    @GET("/api/user/drivers")
    Call<List<Driver>>  getAllDrivers();

    @GET("/api/user/status/{id}")
    Call<Void> updateStatus(@Path("id") String id);

    @POST("/api/user/drivers/available")
    Call<List<Driver>>  getAvailableDrivers(@Body AvailabilityRequest request);

}
