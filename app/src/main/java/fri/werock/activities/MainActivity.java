package fri.werock.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

import fri.werock.R;
import fri.werock.api.ApiError;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.models.User;
import fri.werock.utils.UserTokenStorage;

public class MainActivity extends AuthenticatedActivity implements View.OnTouchListener {
    private TextView textView;
    private Button logout, profile;

    private ImageView img1, img2, img3, img4, img5, img6;
    private TextView img1_name, img2_name, img3_name, img4_name, img5_name, img6_name;
    private TextView img1_tags, img2_tags, img3_tags, img4_tags, img5_tags, img6_tags;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img = findViewById(R.id.img1);
        //textView = findViewById(R.id.text_view);
        this.logout = findViewById(R.id.log_out);
        this.profile = findViewById(R.id.myprofile_button);

        UserTokenStorage userTokenStorage = new UserTokenStorage(MainActivity.this);
        this.logout.setOnClickListener(view -> {
            showDialog(userTokenStorage);
        });
        this.profile.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
        });

        img.setOnTouchListener(this);
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


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == android.view.MotionEvent.ACTION_DOWN) {
            Log.d("TouchTest", "Touch down");
        } else if (motionEvent.getAction() == android.view.MotionEvent.ACTION_UP) {
            Log.d("TouchTest", "Touch up");
        }
        return true;
    }

    void showDialog(UserTokenStorage user){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.log_out_dialog);


        Button yesButton = dialog.findViewById(R.id.logout_yes);
        Button noButton = dialog.findViewById(R.id.logout_no);

        noButton.setOnClickListener(view -> {
            dialog.dismiss();
        });

        yesButton.setOnClickListener(view -> {
            dialog.dismiss();
            user.clear();
            this.finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

        dialog.show();
    }
}