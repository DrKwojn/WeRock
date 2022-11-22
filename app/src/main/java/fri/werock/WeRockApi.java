package fri.werock;

import android.content.Context;

import java.util.List;

import fri.werock.model.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface WeRockApi {
    static WeRockApi create(Context context) {
        String serverUrl = context.getString(R.string.server_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WeRockApi.class);
    }

    @GET("user/list")
    Call<List<User>> getUserList();
}
