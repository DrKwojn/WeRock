package fri.werock.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
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
    private Button previousButton, nextButton;
    private Button homeButton, searchButton;
    private ConstraintLayout r1c1,r1c2,r2c1,r2c2,r3c1,r3c2;

    //UserElementHandler userElementHandler = new UserElementHandler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.log_out);
        profile = findViewById(R.id.myprofile_button);


        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);

        ArrayList<TextView> nameFields = new ArrayList<>();

        nameFields.add(img1_name = findViewById(R.id.img1_name));
        nameFields.add(img2_name = findViewById(R.id.img2_name));
        nameFields.add(img3_name = findViewById(R.id.img3_name));
        nameFields.add(img4_name = findViewById(R.id.img4_name));
        nameFields.add(img5_name = findViewById(R.id.img5_name));
        nameFields.add(img6_name = findViewById(R.id.img6_name));




        img1_tags = findViewById(R.id.img1_tags);
        img2_tags = findViewById(R.id.img2_tags);
        img3_tags = findViewById(R.id.img3_tags);
        img4_tags = findViewById(R.id.img4_tags);
        img5_tags = findViewById(R.id.img5_tags);
        img6_tags = findViewById(R.id.img6_tags);

        previousButton = findViewById(R.id.explore_back_button);
        nextButton = findViewById(R.id.explore_next_button);

        homeButton = findViewById(R.id.home_button);
        searchButton = findViewById(R.id.search_button);

        ArrayList<ConstraintLayout> userTiles = new ArrayList<>();

        userTiles.add(r1c1 = findViewById(R.id.r1c1));
        userTiles.add(r1c2 = findViewById(R.id.r1c2));
        userTiles.add(r2c1 = findViewById(R.id.r2c2));
        userTiles.add(r2c2 = findViewById(R.id.r2c1));
        userTiles.add(r3c1 = findViewById(R.id.r3c1));
        userTiles.add(r3c2 = findViewById(R.id.r3c2));


        ArrayList<String> tempUsernames = new ArrayList<>();



        UserTokenStorage userTokenStorage = new UserTokenStorage(MainActivity.this);
        logout.setOnClickListener(view -> {
            showDialog(userTokenStorage);
        });
        profile.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
        });


        WeRockApi.fetch(this.weRockApi.getUserList(), new WeRockApiCallback<List<User>>() {
            @Override
            public void onResponse(List<User> users) {
                for(User user : users){
                    tempUsernames.add(user.getUsername());
                }

                for(int i=0; i<tempUsernames.size(); i++){
                    nameFields.get(i).setText(tempUsernames.get(i));
                    userTiles.get(i).setVisibility(View.VISIBLE);
                }
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