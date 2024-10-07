package services;

import java.util.List;

import models.Requests.AddCarRequest;
import models.Requests.AvailabilityRequest;
import models.Car;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CarService {

    @GET("api/car")
    Call<List<Car>> getCars();

    @POST("api/car/verif/{id}") // The {id} is a path parameter
    Call<Boolean> verifyCarAvailability(@Path("id") String carId, @Body AvailabilityRequest request);

    @POST("api/car")  // Endpoint for adding a new car
    Call<Void> addCar(@Body AddCarRequest car);
}
