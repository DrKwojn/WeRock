package fri.werock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import fri.werock.api.WeRockApi;
import fri.werock.model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AuthenticatedActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text_view);

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