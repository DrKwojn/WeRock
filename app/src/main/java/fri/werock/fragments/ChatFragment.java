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
import fri.werock.adapters.MessageAdapter;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.api.WeRockApiError;
import fri.werock.models.Message;
import fri.werock.models.User;

public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;

    private static final String ARG_ID = "id";

    private int id;

    public ChatFragment() {}

    public static ChatFragment newInstance(int id) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.id = getArguments().getInt(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        WeRockApi.fetch(((AuthenticatedActivity)this.getActivity()).getWeRockApi().getMessages(id), new WeRockApiCallback<List<Message>>() {
            @Override
            public void onResponse(List<Message> messages) {
                MessageAdapter adapter = new MessageAdapter(ChatFragment.this.getActivity(), messages);
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
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