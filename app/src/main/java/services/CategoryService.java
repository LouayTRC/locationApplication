package services;

import java.util.List;

import models.Category;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CategoryService {
    @GET("api/types")
    Call<List<Category>> getCategorys();

    @POST("api/types") // The {id} is a path parameter
    Call<Boolean> addCategory(@Body String name);
}
