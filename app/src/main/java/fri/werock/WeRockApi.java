package fri.werock;

import java.util.List;

import fri.werock.model.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface WeRockApi {
    static WeRockApi create() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.105.251.232:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WeRockApi.class);
    }

    @GET("user/list")
    Call<List<User>> getUserList();
}
