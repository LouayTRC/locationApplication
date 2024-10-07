package services;

import java.util.List;

import models.Marque;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface MarqueService {

    @GET("api/marque")
    Call<List<Marque>> getMarques();

    @POST("api/marque")
    Call<Boolean> addMarque(@Body String name);

}
