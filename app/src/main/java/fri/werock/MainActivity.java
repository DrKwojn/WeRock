package fri.werock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import fri.werock.api.WeRockApi;
import fri.werock.model.User;
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
        logout = findViewById(R.id.log_out);

        SharedPreferences sharedPreferences = this.getSharedPreferences(this.getString(R.string.app_name), Context.MODE_PRIVATE);
        this.logout.setOnClickListener(view -> {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("token", null);
            editor.apply();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            });

        Call<List<User>> call = this.weRockApi.getUserList();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                int code = response.code();
                if(code != 200) {
                    textView.setText("CODE:" + code);
                    return;
                }

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