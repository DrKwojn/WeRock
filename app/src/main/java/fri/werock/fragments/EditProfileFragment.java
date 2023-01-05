package fri.werock.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fri.werock.R;
import fri.werock.activities.AuthenticatedActivity;
import fri.werock.activities.LoginActivity;

public class EditProfileFragment extends Fragment {
    private Button logout;

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

        this.logout = this.getActivity().findViewById(R.id.logout);
        logout.setOnClickListener(buttonView -> {
            activity.getUserTokenStorage().clear();
            startActivity(new Intent(activity.getApplicationContext(), LoginActivity.class));
        });
    }
}