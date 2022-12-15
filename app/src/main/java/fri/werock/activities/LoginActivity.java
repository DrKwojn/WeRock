package fri.werock.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fri.werock.R;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.api.ApiError;
import fri.werock.models.AuthenticationToken;
import fri.werock.models.UserAccount;
import fri.werock.utils.UserTokenStorage;

public class LoginActivity extends AppCompatActivity {
    private EditText editUsername;
    private EditText editPassword;

    private TextView usernameErr;
    private TextView passwordErr;

    private ImageView layers;

    private Button buttonLogin;
    private Button buttonRegister;
    public String user = "User";
    public String password = "Pass";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserTokenStorage userTokenStorage = new UserTokenStorage(LoginActivity.this);
        WeRockApi weRockApi = WeRockApi.create(this);

        this.layers = this.findViewById(R.id.layers);
        this.layers.setImageResource(R.drawable.layers);

        this.editUsername = this.findViewById(R.id.edit_username);
        this.editPassword = this.findViewById(R.id.edit_password);

        this.usernameErr = this.findViewById(R.id.userErr);
        this.passwordErr = this.findViewById(R.id.passErr);


        this.buttonLogin = this.findViewById(R.id.login_button);
        this.buttonLogin.setOnClickListener(view -> {
            UserAccount userAccount = new UserAccount();
            userAccount.setUsername(this.editUsername.getText().toString().trim());
            userAccount.setPassword(this.editPassword.getText().toString().trim());

            WeRockApi.fetch(weRockApi.login(userAccount), new WeRockApiCallback<AuthenticationToken>() {
                @Override
                public void onResponse(AuthenticationToken authenticationToken) {
                    userTokenStorage.store(authenticationToken.getAccessToken());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

                @Override
                public void onError(ApiError error) {
                    //If username not found
                    if(!userAccount.getUsername().equals(user)) {
                        usernameErr.setVisibility(View.VISIBLE);
                        usernameErr.setText("Username not found");
                    }

                    if(!userAccount.getPassword().equals(password)) {
                        passwordErr.setVisibility(View.VISIBLE);
                        passwordErr.setText("Incorrect password");
                    }


                }

                @Override
                public void onFailure() {
                    //TODO: Connection error
                }
            });
        });

        this.buttonRegister = this.findViewById(R.id.register_button);
        this.buttonRegister.setOnClickListener(view -> {
            startActivity(new Intent(this.getApplicationContext(), RegisterActivity.class));
        });
    }
}