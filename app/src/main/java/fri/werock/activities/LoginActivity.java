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
import android.widget.Toast;

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
                    Intent intent = new Intent(getApplicationContext(), AuthenticatedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }

                @Override
                public void onError(WeRockApiError error) {
                    //If username not found
                    Toast.makeText(LoginActivity.this,error.getMessage(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(LoginActivity.this,String.valueOf(error.getId()), Toast.LENGTH_SHORT).show();
                    if(error.getId()==31) {
                        usernameErr.setText("Incorrect username or password");
                        passwordErr.setText("Incorrect username or password");
                    }


                    else if(error.getId()==30){
                        usernameErr.setText("Missing username or password");
                        passwordErr.setText("Missing username or password");
                    }

                    else{
                        usernameErr.setText("");
                        passwordErr.setText("");
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
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            LoginActivity.this.finish();
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