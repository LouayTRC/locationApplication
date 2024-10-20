package services;

import java.util.List;
import models.Requests.AvailabilityRequest;
import models.Requests.ReserveRequest;
import models.Reservation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ReservationService {

    @POST("api/reservation") // Réserver une voiture
    Call<Reservation> reserver(@Body ReserveRequest request);

    @GET("api/reservations") // Récupérer la liste des réservations
    Call<List<Reservation>> getReservations();
}
