package fri.werock.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fri.werock.R;
import fri.werock.api.ApiError;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.models.UserAccount;

public class RegisterActivity extends AppCompatActivity {
    private EditText editUsername;
    private EditText editEmail;

    private EditText editPassword;
    private EditText editRetryPassword;

    private TextView testText;

    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        WeRockApi weRockApi = WeRockApi.create(this);

        this.editUsername = this.findViewById(R.id.edit_email);
        this.editEmail = this.findViewById(R.id.edit_email);

        this.editPassword = this.findViewById(R.id.edit_username);
        this.editRetryPassword = this.findViewById(R.id.edit_retry_password);

        //this.testText = this.findViewById(R.id.testText);

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

            WeRockApi.fetch(weRockApi.register(userAccount), new WeRockApiCallback<Void>() {
                @Override
                public void onResponse(Void unused) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }

                @Override
                public void onError(ApiError error) {
                    //TODO: Response errors
                }

                @Override
                public void onFailure() {
                    //TODO: Connection error
                }
            });
        });
    }
}