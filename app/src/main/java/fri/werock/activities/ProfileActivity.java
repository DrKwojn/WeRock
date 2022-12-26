package fri.werock.activities;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import fri.werock.R;
import fri.werock.databinding.ActivityProfileBinding;
import fri.werock.utils.AudioPlayer;

public class ProfileActivity extends AppCompatActivity {

private ActivityProfileBinding binding;

    private Button addMedia;
    private ConstraintLayout layout;


    private final int PICK_AUDIO = 1;
    Uri AudioUri;




    AudioPlayer audioPlayer = new AudioPlayer(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addMedia = findViewById(R.id.edit_profile_b);
        layout = findViewById(R.id.audioContainer);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle("WeRock");

        addMedia.setOnClickListener(view -> {
            Intent audio = new Intent();
            audio.setType("audio/*");
            audio.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(Intent.createChooser(audio, "Select Audio"), PICK_AUDIO);


        });



    }
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_AUDIO && resultCode == RESULT_OK) {
            // Audio is Picked in format of URI
            AudioUri = data.getData();
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), AudioUri);

            View player = getLayoutInflater().inflate(R.layout.audio_player_layout_current, null);
            audioPlayer.addPlayer(player, mediaPlayer, layout);

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}