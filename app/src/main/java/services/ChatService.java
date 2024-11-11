package services;

import java.util.List;

import models.Discussion;
import models.Message;
import models.Requests.SendMessageRequest;
import models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatService {

    @POST("/api/chat/add/{sourceId}/{destinationId}")
    Call<Discussion> createDiscussion(@Path("sourceId") String sourceId,@Path("destinationId") String destinationId);

    @POST("/api/chat/send/{discussionId}")
    Call<Message> sendMessage(@Path("discussionId") String discussionId, @Body SendMessageRequest request);

    @GET("/api/chat/{userId}")
    Call<List<Discussion>> getUserDiscussions(@Path("userId") String userId);

    @GET("/api/chat/id/{id}")
    Call<Discussion> getDiscussionById(@Path("id") String id);

    @GET("/api/chat/contact/{id}")
    Call<List<User>> getContacts(@Path("id") String id);
}
