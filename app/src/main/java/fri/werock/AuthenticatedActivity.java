package fri.werock;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import fri.werock.api.WeRockApi;

public class AuthenticatedActivity extends AppCompatActivity {
    protected WeRockApi weRockApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = this.getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        if(!sharedPreferences.contains(this.getString(R.string.token))){
            startActivity(new Intent(this.getApplicationContext(), LoginActivity.class));
        }

        String token = sharedPreferences.getString(this.getString(R.string.token), null);
        if(token == null){
            startActivity(new Intent(this.getApplicationContext(), LoginActivity.class));
        }

        this.weRockApi = WeRockApi.create(this, token);

        //TODO: Check that the token is valid
    }
}