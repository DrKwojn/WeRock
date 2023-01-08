package fri.werock.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX;
import com.google.android.youtube.player.YouTubePlayerView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fri.werock.R;
import fri.werock.activities.AuthenticatedActivity;
import fri.werock.activities.LoginActivity;
import fri.werock.adapters.ExploreUserAdapter;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.api.WeRockApiError;
import fri.werock.models.User;

public class ProfileFragment extends Fragment {
    private int id;

    private TextView name;
    private TextView description;
    private TextView tags;

    private Button chat;
    public ProfileFragment() {}

    public static ProfileFragment newInstance(int id) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("ID", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() == null) {
            return;
        }

        this.id = this.getArguments().getInt("ID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_profile, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = getActivity().findViewById(R.id.name);
        description = getActivity().findViewById(R.id.description);
        tags = getActivity().findViewById(R.id.tags);

        WeRockApi.fetch(((AuthenticatedActivity)this.getActivity()).getWeRockApi().getUser(id), new WeRockApiCallback<User>() {
            @Override
            public void onResponse(User user) {
                name.setText(user.getFullName() != null ? user.getFullName() : user.getUsername());
                description.setText(user.getDescription() != null ? user.getDescription() : "User has no description");
                tags.setText(user.getTags());
            }

            @Override
            public void onError(WeRockApiError error) {
                Toast.makeText(ProfileFragment.this.getActivity(), "Error", Toast.LENGTH_LONG);
            }

            @Override
            public void onFailure() {
                Toast.makeText(ProfileFragment.this.getActivity(), "Failed", Toast.LENGTH_LONG);
            }
        });

        chat = getActivity().findViewById(R.id.start_chat);

        chat.setOnClickListener(v -> {
            ChatFragment chatFragment = ChatFragment.newInstance(ProfileFragment.this.id);
            FragmentManager fragmentManager = ProfileFragment.this.getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, chatFragment).commit();
        });

        String funnyvid = "https://youtu.be/dQw4w9WgXcQ";

        YouTubePlayerSupportFragmentX ytvid = YouTubePlayerSupportFragmentX.newInstance();

        FragmentManager fragmentManager = this.getChildFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.myVideo, ytvid).commit();
        ytvid.initialize("AIzaSyBq3HdLDiOXupsQ-dMvzPTNS1MJB2kKlqc",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        youTubePlayer.cueVideo(ytLinkParser(funnyvid));
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Log.d("FAIL", youTubeInitializationResult.toString());
                    }
                });

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