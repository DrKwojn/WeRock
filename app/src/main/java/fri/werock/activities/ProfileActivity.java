package fri.werock.activities;

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import fri.werock.R;
import fri.werock.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

private ActivityProfileBinding binding;

    private Button addMedia;
    private ConstraintLayout layout;
    private MediaPlayer mediaPlayer;

    private final int PICK_AUDIO = 1;
    Uri AudioUri;

    private int player_id = 1;
    private int previous_playerid = 1;
    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;

    private final ArrayList<Integer> playButtons = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mediaPlayer = MediaPlayer.create(this, R.raw.guitar);
        seekbar = findViewById(R.id.seekBar);

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
            addPlayer();

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        releaseMediaPlayer();


    }

    private void addPlayer(){
        View player = getLayoutInflater().inflate(R.layout.audio_player_layouta, null);

        Button playButton = player.findViewById(R.id.playAudio);
        playButton.setId(View.generateViewId());
        playButtons.add(playButton.getId());


        player.setId(player_id);
        layout.addView(player);

        Log.d("Btn", playButtons.toString());

        ConstraintSet set = new ConstraintSet();
        set.constrainWidth(player_id, layout.getWidth());

        if (player_id > 1) {
            set.connect(player_id, ConstraintSet.TOP,
                    previous_playerid, ConstraintSet.BOTTOM, 8);
        }

        set.applyTo(layout);

        previous_playerid = player_id;
        player_id++;
    }

    private void releaseMediaPlayer() {
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}