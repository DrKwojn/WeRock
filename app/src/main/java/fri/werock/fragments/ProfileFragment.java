package fri.werock.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

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

        AuthenticatedActivity activity = ((AuthenticatedActivity)this.getActivity());
        if(activity == null) {
            //TODO: This is an error ?
            return;
        }

        this.name = this.getActivity().findViewById(R.id.name);

        WeRockApi.fetch(((AuthenticatedActivity)this.getActivity()).getWeRockApi().getUser(id), new WeRockApiCallback<User>() {
            @Override
            public void onResponse(User user) {
                ProfileFragment.this.name.setText(user.getUsername());
            }

            @Override
            public void onError(WeRockApiError error) {

            }

            @Override
            public void onFailure() {

            }
        });
    }
}