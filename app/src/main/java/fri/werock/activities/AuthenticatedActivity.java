package fri.werock.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fri.werock.R;
import fri.werock.api.WeRockApi;
import fri.werock.fragments.EditProfileFragment;
import fri.werock.fragments.ExploreFragment;
import fri.werock.fragments.ProfileFragment;
import fri.werock.utils.UserTokenStorage;

public class AuthenticatedActivity extends AppCompatActivity {
    private UserTokenStorage userTokenStorage;
    private WeRockApi weRockApi;

    private BottomNavigationView navigationView;

    private ExploreFragment exploreFragment;
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

        this.weRockApi = WeRockApi.create(this, token);

        //TODO: Check that the token is valid

        this.exploreFragment = ExploreFragment.newInstance();
        this.editProfileFragment = EditProfileFragment.newInstance();

        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exploreFragment).commit();

        this.navigationView = findViewById(R.id.bottom_navigation);
        this.navigationView.setOnItemSelectedListener(item -> {
            if(item.getItemId() == R.id.action_explore) {
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exploreFragment).commit();
            }else if(item.getItemId() == R.id.action_profile) {
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, editProfileFragment).commit();
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