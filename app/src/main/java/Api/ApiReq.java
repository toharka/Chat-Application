package Api;

import models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiReq {

    @POST("users")
    Call<ResponseBody> createUser(@Body User user);
}
