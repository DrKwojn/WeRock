package fri.werock.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fri.werock.R;
import fri.werock.databinding.ActivityEditProfileBinding;
import fri.werock.utils.AudioPlayer;

public class EditProfileActivity extends AppCompatActivity {

private ActivityEditProfileBinding binding;
    private TextView addMedia, editPicture;
    private ConstraintLayout layout;

    private EditText aboutMe, editTags, editLink;
    private ImageView myProfileImg;

    private Button backButton;

    private final int PICK_AUDIO = 1;
    private final int PICK_IMAGE = 2;
    Uri AudioUri;
    AudioPlayer audioPlayer = new AudioPlayer(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addMedia = findViewById(R.id.addMedia);
        editPicture = findViewById(R.id.edit_profile_pic);
        layout = findViewById(R.id.audioContainer);

        aboutMe = findViewById(R.id.aboutme_txt_input);
        editTags = findViewById(R.id.edit_tags_input);
        myProfileImg = findViewById(R.id.myProfileImg);
        editLink = findViewById(R.id.videoLink);

        backButton = findViewById(R.id.back_button);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        toolbar.setTitle("WeRock");

        String idk = parseYtLink("https://www.youtube.com/watch?v=bsJyoHA2_Iw&ab_channel=AwaisMirza");
        Log.d("URL HEEEEERE", idk);


        aboutMe.setOnFocusChangeListener((view, b) -> {
            if(b) {
                aboutMe.setGravity(Gravity.TOP);
                ViewGroup.LayoutParams params = aboutMe.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = 300;
                aboutMe.setLayoutParams(params);
            }else{
                aboutMe.setGravity(Gravity.NO_GRAVITY);
                ViewGroup.LayoutParams params = aboutMe.getLayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                aboutMe.setLayoutParams(params);
            }
        });


        editLink.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(URLUtil.isValidUrl(charSequence.toString())){
                    editLink.setTextColor(getColor(R.color.add_media_color));
                }else{
                    editLink.setTextColor(getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        editTags.setOnFocusChangeListener((view, b) -> {
            if(!b){
                List<String> tags = parseTags(editTags.getText().toString());
                editTags.setText(tags.toString().substring(1,tags.toString().length()-1));
                editTags.setTextColor(getColor(R.color.add_media_color));
            }else{
                editTags.setTextColor(getColor(R.color.black));
            }
        });

        editPicture.setOnClickListener(view -> {
                Intent image = new Intent();
                image.setType("image/*");
                image.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(image, "Select Image"), PICK_IMAGE);

        });

        backButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), MyProfileActivity.class));
            this.finish();
        });




        addMedia.setOnClickListener(view -> {
            if(layout.getChildCount()<2) {
                Intent audio = new Intent();
                audio.setType("audio/*");
                audio.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(audio, "Select Audio"), PICK_AUDIO);
            }else{

                Toast.makeText(this, "Max number of demos reached", Toast.LENGTH_LONG).show();
            }
        });






    }
    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CODE", String.valueOf(requestCode));
        if(requestCode == PICK_AUDIO) {
                if (resultCode == RESULT_OK) {
                    // Audio is Picked in format of URI
                    AudioUri = data.getData();
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), AudioUri);
                    if (mediaPlayer.getDuration() < 60000) {
                        View player = getLayoutInflater().inflate(R.layout.audio_player_layout, null);
                        audioPlayer.addPlayer(player, mediaPlayer, layout);

                    } else {
                        showDialog();
                    }

                }
        }
        if(requestCode == PICK_IMAGE) {

            if (resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    myProfileImg.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this, "You haven't picked an image", Toast.LENGTH_LONG).show();
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

    public String parseYtLink(String url){
        String pattern = "https?:\\/\\/(?:[0-9A-Z-]+\\.)?(?:youtu\\.be\\/|youtube\\.com\\S*[^\\w\\-\\s])([\\w\\-]{11})(?=[^\\w\\-]|$)(?![?=&+%\\w]*(?:['\"][^<>]*>|<\\/a>))[?=&+%\\w]*";

        Pattern compiledPattern = Pattern.compile(pattern,
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static List<String> parseTags(final String input) {

        List<String> allMatches = new ArrayList<String>();
        final Pattern pattern = Pattern.compile("\\B#([a-z0-9]{2,})(?![~!@#$%^&*()=+_`\\-\\|\\/'\\[\\]\\{\\}]|[?.,]*\\w)", Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            allMatches.add(matcher.group());
        }
        return allMatches;
    }
}