package fri.werock.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import fri.werock.R;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.api.WeRockApiError;
import fri.werock.models.AuthenticationToken;
import fri.werock.models.UserAccount;
import fri.werock.utils.UserTokenStorage;

public class LoginActivity extends AppCompatActivity {
    private EditText editUsername;
    private EditText editPassword;

    private TextView usernameErr;
    private TextView passwordErr;

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


        this.editUsername = this.findViewById(R.id.edit_email);
        this.editPassword = this.findViewById(R.id.edit_username);

        this.usernameErr = this.findViewById(R.id.userErr_log);
        this.passwordErr = this.findViewById(R.id.passErr_log);


        this.buttonLogin = this.findViewById(R.id.del_clip_no);
        this.buttonLogin.setOnClickListener(view -> {
            UserAccount userAccount = new UserAccount();
            userAccount.setUsername(this.editUsername.getText().toString().trim());
            userAccount.setPassword(this.editPassword.getText().toString().trim());

            WeRockApi.fetch(weRockApi.login(userAccount), new WeRockApiCallback<AuthenticationToken>() {
                @Override
                public void onResponse(AuthenticationToken authenticationToken) {
                    userTokenStorage.store(authenticationToken.getAccessToken());
                    startActivity(new Intent(getApplicationContext(), AuthenticatedActivity.class));
                    LoginActivity.this.finish();
                }

                @Override
                public void onError(WeRockApiError error) {

                    Log.d("MSG", error.toString());
                    //If username not found
                    if(!userAccount.getUsername().equals(user)) {
                        usernameErr.setText("Username not found");
                    }else{
                        usernameErr.setText("");
                    }

                    if(!userAccount.getPassword().equals(password)) {
                        passwordErr.setText("Incorrect password");
                    }


                }

                @Override
                public void onFailure() {
                    //TODO: Connection error
                    showDialog();

                }
            });
        });

        this.buttonRegister = this.findViewById(R.id.register_button);
        this.buttonRegister.setOnClickListener(view -> {
            startActivity(new Intent(this.getApplicationContext(), RegisterActivity.class));
        });
    }
    void showDialog(){
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.no_connection_dialog);

        Button closeButton = dialog.findViewById(R.id.del_clip_no);

        closeButton.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

}