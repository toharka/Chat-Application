package Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://10.0.2.2:12345/api/";
    private static Api.ApiClient instance;
    private static Retrofit retrofit;

    private ApiClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized Api.ApiClient getInstance() {
        if (instance == null) {
            instance = new Api.ApiClient();
        }
        return instance;
    }

    public ApiReq getApiInterface() {
        return retrofit.create(ApiReq.class);
    }
}

