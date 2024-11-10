package services;

import models.Requests.LoginRequest;
import models.Requests.LoginResponse;
import models.Requests.SignupRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("api/auth/signup")  // Endpoint for adding a new car
    Call<Void> signup(@Body SignupRequest signupRequest);

    @POST("api/auth/login")  // Endpoint for adding a new car
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
