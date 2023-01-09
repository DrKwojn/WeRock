package fri.werock.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fri.werock.R;
import fri.werock.activities.AuthenticatedActivity;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.api.WeRockApiError;
import fri.werock.fragments.EditProfileFragment;
import fri.werock.fragments.ExploreFragment;
import fri.werock.fragments.ProfileFragment;
import fri.werock.models.User;
import okhttp3.ResponseBody;

public class ExploreUserAdapter extends RecyclerView.Adapter<ExploreUserAdapter.ExploreUserViewHolder> {
    public interface OnUserClickListener {
        void onUserClick(int id);
    }

    private AuthenticatedActivity activity;
    private List<User> users;
    int height;
    int width;
    private OnUserClickListener listener;

    public ExploreUserAdapter(AuthenticatedActivity activity, List<User> users) {
        this.activity = activity;

        Log.d("width", String.valueOf(width));

        this.users = users;
    }

    public void setOnUserClickListener(OnUserClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExploreUserAdapter.ExploreUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater inflater = LayoutInflater.from(activity);
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
        }

        if(users.get(position).getTags() != null) {
            holder.tags.setText(users.get(position).getTags());
        }

        WeRockApi.fetch(this.activity.getWeRockApi().downloadImage(users.get(position).getID()), new WeRockApiCallback<ResponseBody>() {
            @Override
            public void onResponse(ResponseBody body) {
                if(body == null) {
                    return;
                }

                final Bitmap selectedImage = BitmapFactory.decodeStream(body.byteStream());
                holder.image.setImageBitmap(selectedImage);
            }

            @Override
            public void onError(WeRockApiError error) {
            }

            @Override
            public void onFailure() {
            }
        });

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
        private ImageView image;

        public ExploreUserViewHolder(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.name);
            this.description = itemView.findViewById(R.id.description);
            this.tags = itemView.findViewById(R.id.tags);
            image = itemView.findViewById(R.id.profile_image);

        }
    }
}
