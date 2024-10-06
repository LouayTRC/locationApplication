package services;

import java.util.List;

import models.Car;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CarService {

    @GET("api/car") // Replace with your actual endpoint
    Call<List<Car>> getCars();
}
