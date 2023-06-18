package Api;

import java.util.Map;

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

    @GET("users/{username}")
    Call<ResponseBody> getUserInfo(@Header("Authorization") String token, @Path("username") String username);


}
