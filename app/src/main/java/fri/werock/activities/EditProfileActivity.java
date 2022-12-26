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

import android.view.View;
import android.view.Window;
import android.widget.Button;

import fri.werock.R;
import fri.werock.databinding.ActivityProfileBinding;
import fri.werock.utils.AudioPlayer;

public class EditProfileActivity extends AppCompatActivity {

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
            if(layout.getChildCount()<2) {
                Intent audio = new Intent();
                audio.setType("audio/*");
                audio.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(audio, "Select Audio"), PICK_AUDIO);
            }
        });



    }
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(layout.getChildCount()<2) {
            if (requestCode == PICK_AUDIO && resultCode == RESULT_OK) {
                // Audio is Picked in format of URI
                AudioUri = data.getData();
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), AudioUri);

                if(mediaPlayer.getDuration()<60000) {
                    View player = getLayoutInflater().inflate(R.layout.audio_player_layout_current, null);
                    audioPlayer.addPlayer(player, mediaPlayer, layout);
                }else{
                    showDialog();
                }

            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    void showDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.clip_too_big_dialog);


        Button closeButton = dialog.findViewById(R.id.clip_dia_close);

        closeButton.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }
}