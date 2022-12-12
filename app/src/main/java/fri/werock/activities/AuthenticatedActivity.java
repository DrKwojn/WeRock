package fri.werock.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import fri.werock.R;
import fri.werock.api.WeRockApi;
import fri.werock.utils.UserTokenStorage;

public class AuthenticatedActivity extends AppCompatActivity {
    protected UserTokenStorage userTokenStorage;
    protected WeRockApi weRockApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.userTokenStorage = new UserTokenStorage(this);

        String token = this.userTokenStorage.fetch();
        if(token == null){
            startActivity(new Intent(this.getApplicationContext(), LoginActivity.class));
        }

        this.weRockApi = WeRockApi.create(this, token);

        //TODO: Check that the token is valid
    }
}