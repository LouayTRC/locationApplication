package services;

import java.util.List;

import models.Car;
import models.Role;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RoleService {

    @GET("api/role")
    Call<List<Role>> getRoles();
}
