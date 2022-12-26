package fri.werock.utils;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import fri.werock.R;

public class AudioPlayer {
    private int player_id;
    private int previous_playerid;
    View player;
    Context context;

    public AudioPlayer( Context ctx){
        player_id=1;
        previous_playerid=1;
        context=ctx;
    }


    public void addPlayer(View player ,MediaPlayer mediaPlayer, ConstraintLayout layout){

        SeekBar seek = player.findViewById(R.id.seekBar);
        seek.setId(View.generateViewId());
        seek.setMax(mediaPlayer.getDuration());

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress);
                    seek.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button playButton = player.findViewById(R.id.playAudio);
        playButton.setId(View.generateViewId());

        playButton.setOnClickListener(view -> {
            mediaPlayer.start();
        });

        Button delete = player.findViewById(R.id.delete_player);

        delete.setOnClickListener(view -> {
            player_id=player.getId();
            showDialog(layout);
        });

        player.setId(player_id);
        layout.addView(player);

        ConstraintSet set = new ConstraintSet();
        set.constrainWidth(player_id, layout.getWidth());

        if (player_id >1) {
            set.connect(player_id, ConstraintSet.TOP,
                    previous_playerid, ConstraintSet.BOTTOM, 8);
        }

        set.applyTo(layout);

        previous_playerid = player_id;
        player_id++;

    }

    private void play(MediaPlayer mediaPlayer, Button playButton) {

        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playButton.setBackgroundResource(R.drawable.playbutton);
        } else {
            if(mediaPlayer !=null)
            pause(mediaPlayer,playButton);
        }

    }

    private void pause(MediaPlayer mediaPlayer, Button playButton) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playButton.setBackgroundResource(R.drawable.ic_pass);

        }

    }

    void showDialog(ConstraintLayout layout){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.delete_audio_dialog);


        Button closeButton = dialog.findViewById(R.id.del_clip_no);
        Button deleteButton = dialog.findViewById(R.id.del_clip_yes);

        closeButton.setOnClickListener(view -> {
            dialog.dismiss();
        });

        deleteButton.setOnClickListener(view -> {
            dialog.dismiss();
            deletePlayer(layout);
        });

        dialog.show();
    }

    void deletePlayer(ConstraintLayout layout){

        if(player_id>1) {
            previous_playerid = player_id - 1;
        }

        layout.removeView(layout.findViewById(player_id));
    }


    public int getPlayer_id() {
        return player_id;
    }

    public int getPrevious_playerid() {
        return previous_playerid;
    }
}
