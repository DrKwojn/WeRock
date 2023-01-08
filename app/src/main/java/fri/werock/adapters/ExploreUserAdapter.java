package fri.werock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fri.werock.R;
import fri.werock.activities.AuthenticatedActivity;
import fri.werock.fragments.ProfileFragment;
import fri.werock.models.User;

public class ExploreUserAdapter extends RecyclerView.Adapter<ExploreUserAdapter.ExploreUserViewHolder> {
    public interface OnUserClickListener {
        void onUserClick(int id);
    }

    private Context context;
    private List<User> users;

    private OnUserClickListener listener;

    public ExploreUserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExploreUserAdapter.ExploreUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_explore_user, parent, false);
        return new ExploreUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreUserAdapter.ExploreUserViewHolder holder, int position) {
        holder.id = users.get(position).getID();

        String fullName = users.get(position).getFullName();
        if(fullName == null) {
            fullName = users.get(position).getUsername();
        }

        holder.name.setText(fullName);
        if(users.get(position).getDescription() != null) {
            holder.description.setText(users.get(position).getDescription());
        } else {
            //TODO: Remove this for testing only
            holder.description.setText("User has no description set");
        }

        if(users.get(position).getTags() != null) {
            holder.description.setText(users.get(position).getTags());
        }

        if(listener != null) {
            holder.itemView.setOnClickListener(v -> {
                listener.onUserClick(holder.id);
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public static class ExploreUserViewHolder extends RecyclerView.ViewHolder {
        private int id;
        private TextView name;
        private TextView description;
        private TextView tags;

        public ExploreUserViewHolder(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.name);
            this.description = itemView.findViewById(R.id.description);
            this.tags = itemView.findViewById(R.id.tags);
        }
    }
}
