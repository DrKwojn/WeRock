package fri.werock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockAuthApi;
import fri.werock.model.AuthenticationToken;
import fri.werock.model.User;
import fri.werock.model.UserAccount;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText editUsername;
    private EditText editPassword;

    private Button buttonLogin;
    private Button buttonRegister;

    private TextView testText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = this.getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);

        WeRockAuthApi weRockAuthApi = WeRockAuthApi.create(this);

        this.editUsername = this.findViewById(R.id.edit_username);
        this.editPassword = this.findViewById(R.id.edit_password);

        //this.testText = this.findViewById(R.id.testText);

        this.buttonLogin = this.findViewById(R.id.login_button);
        this.buttonLogin.setOnClickListener(view -> {
            UserAccount userAccount = new UserAccount();
            userAccount.setUsername(this.editUsername.getText().toString().trim());
            userAccount.setPassword(this.editPassword.getText().toString().trim());

            Call<AuthenticationToken> call = weRockAuthApi.login(userAccount);
            call.enqueue(new Callback<AuthenticationToken>() {
                @Override
                public void onResponse(Call<AuthenticationToken> call, Response<AuthenticationToken> response) {
                    //testText.setText(response.toString());
                    if(response.code() != 200 || response.body() == null){
                        return;
                    }

                    String accessToken = response.body().getAccessToken();

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.token), accessToken);
                    editor.apply();
                    editor.commit();

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

                @Override
                public void onFailure(Call<AuthenticationToken> call, Throwable t) {

                }
            });
        });

        this.buttonRegister = this.findViewById(R.id.register_button);
        this.buttonRegister.setOnClickListener(view -> {
            startActivity(new Intent(this.getApplicationContext(), RegisterActivity.class));
        });
    }
}