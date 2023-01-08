package fri.werock.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fri.werock.R;
import fri.werock.activities.AuthenticatedActivity;
import fri.werock.adapters.ChatUserAdapter;
import fri.werock.adapters.ExploreUserAdapter;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.api.WeRockApiError;
import fri.werock.models.User;

public class FriendFragment extends Fragment {
    private RecyclerView recyclerView;

    public FriendFragment() {}

    public static FriendFragment newInstance() {
        FriendFragment fragment = new FriendFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.user_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        WeRockApi.fetch(((AuthenticatedActivity)this.getActivity()).getWeRockApi().getChatList(), new WeRockApiCallback<List<User>>() {
            @Override
            public void onResponse(List<User> users) {
                ChatUserAdapter adapter = new ChatUserAdapter(FriendFragment.this.getActivity(), users);
                adapter.setOnUserClickListener(id -> {
                    ChatFragment chatFragment = ChatFragment.newInstance(id);
                    FragmentManager fragmentManager = FriendFragment.this.getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, chatFragment).commit();
                });
                recyclerView.setAdapter(adapter);
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