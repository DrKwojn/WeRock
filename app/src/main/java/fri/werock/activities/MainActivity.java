package fri.werock.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fri.werock.R;
import fri.werock.activities.AuthenticatedActivity;
import fri.werock.activities.LoginActivity;
import fri.werock.api.ApiError;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.models.User;
import fri.werock.utils.UserTokenStorage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AuthenticatedActivity {
    private TextView textView;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);
        this.logout = findViewById(R.id.log_out);

        UserTokenStorage userTokenStorage = new UserTokenStorage(MainActivity.this);
        this.logout.setOnClickListener(view -> {
            userTokenStorage.clear();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

        WeRockApi.fetch(this.weRockApi.getUserList(), new WeRockApiCallback<List<User>>() {
            @Override
            public void onResponse(List<User> users) {
                String text = "";
                for(User user : users){
                    text += user.getUsername() + "\n";
                }

                textView.setText(text);
            }

            @Override
            public void onError(ApiError error) {
                textView.setText("Error");
            }

            @Override
            public void onFailure() {
                textView.setText("Failure");
            }
        });
    }
}