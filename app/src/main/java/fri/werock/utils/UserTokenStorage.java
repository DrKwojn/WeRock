package fri.werock.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import fri.werock.R;

public class UserTokenStorage {
    private String appString;
    private String tokenString;

    private SharedPreferences sharedPreferences;

    public UserTokenStorage(Context context) {
        this.appString = context.getString(R.string.app_name);
        this.tokenString = context.getString(R.string.token);

        this.sharedPreferences = context.getSharedPreferences(this.appString, Context.MODE_PRIVATE);
    }

    public String fetch() {
        return this.sharedPreferences.getString(this.tokenString, null);
    }

    @SuppressLint("ApplySharedPref")
    public void store(String token) {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putString(this.tokenString, token);
        editor.commit();
    }

    @SuppressLint("ApplySharedPref")
    public void clear() {
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.remove(this.tokenString);
        editor.commit();
    }
}
