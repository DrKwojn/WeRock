package fri.werock.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fri.werock.R;
import fri.werock.activities.AuthenticatedActivity;
import fri.werock.activities.LoginActivity;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.api.WeRockApiError;
import fri.werock.models.User;
import fri.werock.utils.AudioPlayer;
import fri.werock.utils.FileUtil;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

import static android.app.Activity.RESULT_OK;

public class EditProfileFragment extends Fragment {
    private Button logout;
    private TextView addMedia, editPicture;
    private ConstraintLayout layout;

    private EditText aboutMe, editTags, editLink;
    private ImageView myProfileImg;

    private Button backButton;

    private final int PICK_AUDIO = 1;
    private final int PICK_IMAGE = 2;
    Uri AudioUri;
    AudioPlayer audioPlayer = new AudioPlayer(this.getContext());

    public EditProfileFragment() {}

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AuthenticatedActivity activity = ((AuthenticatedActivity)this.getActivity());
        if(activity == null) {
            //TODO: This is an error ?
            return;

        }

        addMedia = this.getActivity().findViewById(R.id.addMedia);
        editPicture = this.getActivity().findViewById(R.id.edit_profile_pic);
        layout = this.getActivity().findViewById(R.id.audioContainer);

        aboutMe = this.getActivity().findViewById(R.id.aboutme_txt_input);
        editTags = this.getActivity().findViewById(R.id.edit_tags_input);
        myProfileImg = this.getActivity().findViewById(R.id.myProfileImg);
        editLink = this.getActivity().findViewById(R.id.videoLink);

        WeRockApi.fetch(this.getAuthActivity().getWeRockApi().downloadImage(), new WeRockApiCallback<ResponseBody>() {
            @Override
            public void onResponse(ResponseBody body) {
                final Bitmap selectedImage = BitmapFactory.decodeStream(body.byteStream());
                myProfileImg.setImageBitmap(selectedImage);
                Toast.makeText(EditProfileFragment.this.getActivity(), "We downloaded the image :)", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(WeRockApiError error) {
                Toast.makeText(EditProfileFragment.this.getActivity(), "Error :(", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(EditProfileFragment.this.getActivity(), "Failure :(", Toast.LENGTH_LONG).show();
            }
        });

        aboutMe.setOnFocusChangeListener((v, b) -> {
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
                    editLink.setTextColor(getResources().getColor(R.color.add_media_color));
                }else{
                    editLink.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTags.setOnFocusChangeListener((v, b) -> {
            if(!b){
                List<String> tags = parseTags(editTags.getText().toString());
                editTags.setText(tags.toString().substring(1,tags.toString().length()-1));
                editTags.setTextColor(this.getActivity().getColor(R.color.add_media_color));
            }else{
                editTags.setTextColor(this.getActivity().getColor(R.color.black));
            }
        });

        editPicture.setOnClickListener(v -> {
            Intent image = new Intent();
            image.setType("image/*");
            image.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(image, "Select Image"), PICK_IMAGE);

        });

        addMedia.setOnClickListener(v-> {
            if(layout.getChildCount()<2) {
                Intent audio = new Intent();
                audio.setType("audio/*");
                audio.setAction(Intent.ACTION_OPEN_DOCUMENT);
                startActivityForResult(Intent.createChooser(audio, "Select Audio"), PICK_AUDIO);
            }else{

                Toast.makeText(this.getActivity(), "Max number of demos reached", Toast.LENGTH_LONG).show();
            }
        });

        this.logout = this.getActivity().findViewById(R.id.logout);
        logout.setOnClickListener(buttonView -> {
            activity.getUserTokenStorage().clear();
            startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class));
        });
    }

    public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("CODE", String.valueOf(requestCode));
        if(requestCode == PICK_AUDIO) {
            if (resultCode == RESULT_OK) {
                // Audio is Picked in format of URI
                AudioUri = data.getData();
                MediaPlayer mediaPlayer = MediaPlayer.create(this.getActivity().getApplicationContext(), AudioUri);
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
                    InputStream imageStream = this.getActivity().getContentResolver().openInputStream(imageUri);
                    MultipartBody.Part part = FileUtil.fileRequestBody(imageStream, "file", ".jpg", "image/*");

                    WeRockApi.fetch(this.getAuthActivity().getWeRockApi().uploadImage(part), new WeRockApiCallback<Void>() {
                        @Override
                        public void onResponse(Void v) {
                            Toast.makeText(EditProfileFragment.this.getActivity(), "We uploaded the image :)", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(WeRockApiError error) {
                            Toast.makeText(EditProfileFragment.this.getActivity(), "Error :(", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(EditProfileFragment.this.getActivity(), "Failure :(", Toast.LENGTH_LONG).show();
                        }
                    });

                    imageStream = this.getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    myProfileImg.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this.getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(this.getActivity(), "You haven't picked an image", Toast.LENGTH_LONG).show();
            }
        }

    }

    public AuthenticatedActivity getAuthActivity() {
        return ((AuthenticatedActivity)this.getActivity());
    }

    void showDialog(){
        final Dialog dialog = new Dialog(this.getActivity());
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