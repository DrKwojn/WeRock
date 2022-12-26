package fri.werock.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;

import fri.werock.R;
import fri.werock.databinding.ActivityMyprofileBinding;

public class MyProfileActivity extends AppCompatActivity {

    private ActivityMyprofileBinding binding;

    private Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMyprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editProfile = findViewById(R.id.addMedia);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle("WeRock");

        editProfile.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}