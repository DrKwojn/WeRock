package fri.werock.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import fri.werock.R;
import fri.werock.api.WeRockApi;
import fri.werock.api.WeRockApiCallback;
import fri.werock.api.WeRockApiError;
import fri.werock.fragments.EditProfileFragment;
import fri.werock.fragments.ExploreFragment;
import fri.werock.fragments.FriendFragment;
import fri.werock.fragments.ProfileFragment;
import fri.werock.utils.UserTokenStorage;
import okhttp3.ResponseBody;

public class AuthenticatedActivity extends AppCompatActivity {
    private UserTokenStorage userTokenStorage;
    private WeRockApi weRockApi;

    private BottomNavigationView navigationView;

    private ExploreFragment exploreFragment;
    private FriendFragment friendFragment;
    private EditProfileFragment editProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticated);

        this.userTokenStorage = new UserTokenStorage(this);

        String token = this.userTokenStorage.fetch();
        if(token == null){
            startActivity(new Intent(this.getApplicationContext(), LoginActivity.class));
        }

        this.exploreFragment = ExploreFragment.newInstance();
        this.friendFragment = FriendFragment.newInstance();
        this.editProfileFragment = EditProfileFragment.newInstance();

        this.weRockApi = WeRockApi.create(this, token);
        WeRockApi.fetch(this.getWeRockApi().validate(), new WeRockApiCallback<Void>() {
            @Override
            public void onResponse(Void v) {}

            @Override
            public void onError(WeRockApiError error) {
                AuthenticatedActivity.this.userTokenStorage.clear();
                startActivity(new Intent(AuthenticatedActivity.this.getApplicationContext(), LoginActivity.class));
            }

            @Override
            public void onFailure() {
                AuthenticatedActivity.this.userTokenStorage.clear();
                startActivity(new Intent(AuthenticatedActivity.this.getApplicationContext(), LoginActivity.class));
            }
        });

        AuthenticatedActivity activity = AuthenticatedActivity.this;
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exploreFragment).commit();

        activity.navigationView = findViewById(R.id.bottom_navigation);
        activity.navigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.action_explore) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exploreFragment).commit();
            }else if(item.getItemId() == R.id.action_chat) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, friendFragment).commit();
            }else if(item.getItemId() == R.id.action_profile) {
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editProfileFragment).commit();
            }

            return false;
        });
    }

    public UserTokenStorage getUserTokenStorage() {
        return this.userTokenStorage;
    }

    public WeRockApi getWeRockApi() {
        return this.weRockApi;
    }
}