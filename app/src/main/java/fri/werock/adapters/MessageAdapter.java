package fri.werock.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fri.werock.R;
import fri.werock.models.Message;

public class MessageAdapter  extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private Context context;
    private int userId;
    private List<Message> messages;

    public MessageAdapter(Context context, int userId, List<Message> messages) {
        this.context = context;
        this.userId = userId;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_message, parent, false);
        return new MessageAdapter.MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        int id = this.messages.get(position).getFrom();
        String text = this.messages.get(position).getText();
        if(id == this.userId) {
            holder.messageLeft.setText(text);
            holder.layoutLeft.setVisibility(View.VISIBLE);
            holder.layoutRight.setVisibility(View.GONE);
        } else {
            holder.messageRight.setText(text);
            holder.layoutLeft.setVisibility(View.GONE);
            holder.layoutRight.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout layoutLeft;
        private LinearLayout layoutRight;

        private TextView messageLeft;
        private TextView messageRight;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            this.layoutLeft = itemView.findViewById(R.id.message_wrapper_left);
            this.layoutRight = itemView.findViewById(R.id.message_wrapper_right);

            this.messageLeft = itemView.findViewById(R.id.message_left);
            this.messageRight = itemView.findViewById(R.id.message_right);
        }
    }
}
