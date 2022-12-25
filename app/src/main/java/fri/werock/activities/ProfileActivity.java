package fri.werock.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import fri.werock.R;
import fri.werock.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

private ActivityProfileBinding binding;

    private Button b1,b2,b3,b4;

    private MediaPlayer mediaPlayer;

    private SeekBar seekbar;
    private TextView tx1,tx2,tx3;

    public static int oneTimeOnly = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mediaPlayer = MediaPlayer.create(this, R.raw.guitar);
        seekbar = findViewById(R.id.seekBar);

        //MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sound_file_1);
        //mediaPlayer.start(); // no need to call prepare(); create() does that for you

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle("WeRock");

    }
}