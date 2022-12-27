package fri.werock.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fri.werock.R;
import fri.werock.databinding.ActivityMyprofileBinding;

public class MyProfileActivity extends YouTubeBaseActivity {

    private ActivityMyprofileBinding binding;

    private Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMyprofileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editProfile = findViewById(R.id.edit_profile);

        //Toolbar toolbar = binding.toolbar;
        //setSupportActionBar(toolbar);
        //toolbar.setTitle("WeRock");


        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.myVideo);

        String funnyvid = "https://youtu.be/dQw4w9WgXcQ";

        youTubePlayerView.initialize("AIzaSyBq3HdLDiOXupsQ-dMvzPTNS1MJB2kKlqc",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        youTubePlayer.loadVideo(ytLinkParser(funnyvid));
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Log.d("FAIL", youTubeInitializationResult.toString());
                    }
                });


        editProfile.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
        });



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public String ytLinkParser(String url){
        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}