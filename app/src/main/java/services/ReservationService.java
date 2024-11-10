package services;

import java.util.List;

import models.Car;
import models.Requests.AvailabilityRequest;
import models.Requests.ReserveRequest;
import models.Reservation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ReservationService {

    @POST("api/reservation") // Réserver une voiture
    Call<Reservation> reserver(@Body ReserveRequest request);

    @GET("api/reservation/{id}") // Récupérer la liste des réservations
    Call<Reservation> getReservationById(@Path("id") String reservationId);

    @GET("api/reservation/user/{id}") // Récupérer la liste des réservations
    Call<List<Reservation>> getReservationsByUserId(@Path("id") String userId);

    @GET("api/reservation") // Récupérer la liste des réservations
    Call<List<Reservation>> getReservations();

    @PUT("api/reservation/{id}/{status}")
    Call<Reservation> updateReservationStatus(@Path("id") String reservationId, @Path("status") Integer status);
}
