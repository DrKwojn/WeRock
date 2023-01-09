package fri.werock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fri.werock.R;
import fri.werock.models.User;

public class ChatUserAdapter  extends RecyclerView.Adapter<ChatUserAdapter.ChatUserViewHolder>{
    public interface OnUserClickListener {
        void onUserClick(int id);
    }

    private Context context;
    private List<User> users;

    private ChatUserAdapter.OnUserClickListener listener;

    public ChatUserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    public void setOnUserClickListener(ChatUserAdapter.OnUserClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatUserAdapter.ChatUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_chat_user, parent, false);
        return new ChatUserAdapter.ChatUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserAdapter.ChatUserViewHolder holder, int position) {
        holder.id = users.get(position).getID();
        String name = users.get(position).getFullName() != null ? users.get(position).getFullName() : users.get(position).getUsername() ;
        holder.name.setText(name);

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

    public static class ChatUserViewHolder extends RecyclerView.ViewHolder {
        private int id;
        private TextView name;

        public ChatUserViewHolder(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.name);
        }
    }
}
