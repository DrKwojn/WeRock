package fri.werock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.common.SignInButton;

import java.util.List;

import fri.werock.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

        WeRockApi weRockApi = WeRockApi.create(this);
        Call<List<User>> call = weRockApi.getUserList();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                String text = "";
                List<User> users = response.body();
                for(User user : users){
                    text += user.getUsername() + "\n";
                }

                textView.setText(text);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                textView.setText("Error:\n" + t.getMessage());
            }
        });
    }
}