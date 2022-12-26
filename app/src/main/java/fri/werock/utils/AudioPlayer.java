package fri.werock.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import java.util.concurrent.TimeUnit;

import fri.werock.R;

public class AudioPlayer {
    private int player_id;
    private int previous_playerid;
    Context context;


    public AudioPlayer(Context ctx){
        player_id=1;
        previous_playerid=1;
        context=ctx;

    }


    public void addPlayer(View player ,MediaPlayer mediaPlayer, ConstraintLayout layout){

        SeekBar seek = player.findViewById(R.id.seekBar);
        seek.setId(View.generateViewId());
        seek.setMax(mediaPlayer.getDuration());

        TextView duration = player.findViewById(R.id.audio_duration);
        duration.setId(View.generateViewId());

        int d_len = mediaPlayer.getDuration();
        duration.setText(formatDuration(d_len));

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){

                    mediaPlayer.seekTo(progress);
                    seek.setProgress(progress);

                    duration.setText(formatDuration(mediaPlayer.getDuration()-progress));

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
            play(mediaPlayer, playButton);
        });

        Button delete = player.findViewById(R.id.delete_player);

        delete.setOnClickListener(view -> {
            showDialog(mediaPlayer, layout, player.getId());
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
            playButton.setBackgroundResource(R.drawable.pause_btn);
        } else {
            if(mediaPlayer !=null){
                pause(mediaPlayer, playButton);
            }
        }

    }

    private void pause(MediaPlayer mediaPlayer, Button playButton) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playButton.setBackgroundResource(R.drawable.playbutton);

        }

    }

    void showDialog(MediaPlayer mediaPlayer, ConstraintLayout layout, int del_id){
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
            mediaPlayer.release();
            deletePlayer(layout, del_id);
        });

        dialog.show();
    }

    void deletePlayer(ConstraintLayout layout, int del_id){
        layout.removeView(layout.getViewById(del_id));
    }

    @SuppressLint("DefaultLocale")
    private String formatDuration(long duration) {
        long minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS);
        long seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS)
                - minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES);

        return String.format("%02d:%02d", minutes, seconds);
    }


    public int getPlayer_id() {
        return player_id;
    }

    public int getPrevious_playerid() {
        return previous_playerid;
    }

}
