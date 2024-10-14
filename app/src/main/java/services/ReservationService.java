package services;

import models.Requests.AvailabilityRequest;
import models.Requests.ReserveRequest;
import models.Reservation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservationService {

    @POST("api/reservation") // The {id} is a path parameter
    Call<Reservation> reserver(@Body ReserveRequest request);

}
