package fri.werock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import fri.werock.api.WeRockAuthApi;
import fri.werock.model.AuthenticationToken;
import fri.werock.model.UserAccount;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText editUsername;
    private EditText editEmail;

    private EditText editPassword;
    private EditText editRetryPassword;

    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        WeRockAuthApi weRockAuthApi = WeRockAuthApi.create(this);

        this.editUsername = this.findViewById(R.id.edit_username);
        this.editEmail = this.findViewById(R.id.edit_email);

        this.editPassword = this.findViewById(R.id.edit_password);
        this.editRetryPassword = this.findViewById(R.id.edit_retry_password);

        this.buttonRegister = this.findViewById(R.id.register_button);
        this.buttonRegister.setOnClickListener(view -> {
            String password = this.editPassword.getText().toString();
            if(!password.equals(this.editRetryPassword.getText().toString())){
                return;
            }

            UserAccount userAccount = new UserAccount();
            userAccount.setUsername(this.editUsername.getText().toString());
            userAccount.setEmail(this.editEmail.getText().toString());
            userAccount.setPassword(password);

            Call<Void> call = weRockAuthApi.register(userAccount);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.code() != 200){
                        return;
                    }

                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        });
    }
}