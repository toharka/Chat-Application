package Api;

import com.example.chatapplication.Chat;

import java.util.List;
import java.util.Map;

import models.ChatModel;
import models.Message;
import models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiReq {

    @POST("users")
    Call<ResponseBody> createUser(@Body User user);

    @POST("Tokens")
    Call<ResponseBody> connection(@Body Map<String, String> credentials);

    @POST("device")
    Call<ResponseBody> sentDeviceToken (@Body  Map<String, String> credentials);

    @GET("users/{username}")
    Call<ResponseBody> getUserInfo(@Header("Authorization") String token, @Path("username") String username);


    @GET("Chats")
    Call<List<ChatModel>> getChats(@Header("Authorization") String token);

    @POST("Chats")
    Call<ChatModel> createChat(@Header("Authorization") String token, @Body Map<String, String> user);

    @POST("Chats/{chatId}/Messages")
    Call<Message> sendMessage(@Header("Authorization") String token, @Path("chatId") int chatId, @Body Map<String, String> body);

    @GET("Chats/{chatId}/Messages")
    Call<List<Message>> getChatMessages(@Header("Authorization") String token, @Path("chatId") int chatId);

}
