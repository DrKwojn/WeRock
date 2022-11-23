package fri.werock.api;

import android.content.Context;

import java.util.List;

import fri.werock.R;
import fri.werock.model.AuthenticationToken;
import fri.werock.model.User;
import fri.werock.model.UserAccount;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface WeRockAuthApi {
    static WeRockAuthApi create(Context context) {
        String serverUrl = context.getString(R.string.server_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WeRockAuthApi.class);
    }

    @POST("register")
    Call<Void> register(@Body UserAccount userAccount);

    @POST("login")
    Call<AuthenticationToken> login(@Body UserAccount userAccount);
}
