package fri.werock.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fri.werock.R;
import fri.werock.api.ApiError;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.models.User;
import fri.werock.utils.UserTokenStorage;

public class MainActivity extends AuthenticatedActivity {
    private TextView textView;
    private Button logout, profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textView = findViewById(R.id.text_view);
        this.logout = findViewById(R.id.log_out);
        this.profile = findViewById(R.id.myprofile_button);

        UserTokenStorage userTokenStorage = new UserTokenStorage(MainActivity.this);
        this.logout.setOnClickListener(view -> {
            userTokenStorage.clear();
            this.finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

        });
        this.profile.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
        });


        WeRockApi.fetch(this.weRockApi.getUserList(), new WeRockApiCallback<List<User>>() {
            @Override
            public void onResponse(List<User> users) {
                String text = "";
                //for(User user : users){
                //    text += user.getUsername() + "\n";
                //}

               // textView.setText(text);
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