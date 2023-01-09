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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fri.werock.R;
import fri.werock.api.WeRockApiError;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.models.UserAccount;

public class RegisterActivity extends AppCompatActivity {
    private EditText editUsername;
    private EditText editEmail;

    private EditText editPassword;
    private EditText editRetryPassword;

    private TextView emailErr;
    private TextView usernameErr;
    private TextView passwordErr;
    private TextView passConfErr;

    private TextView testText;

    private Button buttonRegister;



    private String pattern = "^(?=.*[0-9])(?=.*[a-zA-Z]).{6,20}$";
    private Pattern p = Pattern.compile(pattern);

    private static boolean matchesPattern(String pass, Pattern p) {
        Matcher matcher = p.matcher(pass);
        return matcher.matches();
    }

    void showDialog(){
        final Dialog dialog = new Dialog(RegisterActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.no_connection_dialog);

        Button closeButton = dialog.findViewById(R.id.del_clip_no);

        closeButton.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        WeRockApi weRockApi = WeRockApi.create(this);

        this.editUsername = this.findViewById(R.id.edit_username);
        this.editEmail = this.findViewById(R.id.edit_email);

        this.editPassword = this.findViewById(R.id.edit_password);
        this.editRetryPassword = this.findViewById(R.id.edit_retry_password);

        //ERRORS
        this.emailErr = this.findViewById(R.id.emailErr_reg);
        this.usernameErr = this.findViewById(R.id.userErr_reg);
        this.passwordErr = this.findViewById(R.id.passErr_reg);
        this.passConfErr = this.findViewById(R.id.passConfErr_reg);

        this.buttonRegister = this.findViewById(R.id.register_button);
        this.buttonRegister.setOnClickListener(view -> {

            String password = this.editPassword.getText().toString().trim();
            if(!password.equals(this.editRetryPassword.getText().toString().trim())){
                passConfErr.setText("Passwords must match!");
                return;

           }else{
               passConfErr.setText("");
               if(!matchesPattern(password, p)) {
                   passwordErr.setText("6-20 characters required!");
                   return;
                }else{
                  passwordErr.setText("");
              }
         }


            UserAccount userAccount = new UserAccount();
            userAccount.setUsername(this.editUsername.getText().toString().trim());
            userAccount.setEmail(this.editEmail.getText().toString().trim());
            userAccount.setPassword(password);

            WeRockApi.fetch(weRockApi.register(userAccount), new WeRockApiCallback<Void>() {
                @Override
                public void onResponse(Void unused) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    RegisterActivity.this.finish();
                }

                @Override
                public void onError(WeRockApiError error) {
                    if(error.getId() == 21){
                        emailErr.setText("E-mail or username already exists");
                    }else{
                        emailErr.setText("");
                    }
                    if(error.getId() == 21){
                        usernameErr.setText("Username or e-mail already exists");

                    }else{
                        usernameErr.setText("");
                    }
                    if(!matchesPattern(userAccount.getPassword(), p)){

                        passwordErr.setText("8 characters (numbers, letters and special) required!");
                    }else {
                        passwordErr.setText("");
                    }
                }

                @Override
                public void onFailure() {
                    Log.d("MESSAGEEE", "Fail");
                    //TODO: Connection error
                    showDialog();
                }
            });
        });
    }
}