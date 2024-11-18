package services;

import models.Requests.LoginRequest;
import models.Requests.LoginResponse;
import models.Requests.SignupClientRequest;
import models.Requests.SignupDriverRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("api/auth/signupC")  // Endpoint for adding a new car
    Call<Void> signupC(@Body SignupClientRequest signupClientRequest);

    @POST("api/auth/signupD")  // Endpoint for adding a new car
    Call<Void> signupD(@Body SignupDriverRequest signupDriverRequest);

    @POST("api/auth/login")  // Endpoint for adding a new car
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
