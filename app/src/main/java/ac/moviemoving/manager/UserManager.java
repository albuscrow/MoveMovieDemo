package ac.moviemoving.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ac on 7/23/16.
 * todo some describe
 */
public class UserManager {
    private Context context;
    public static final String IS_LOGIN_KEY = "is_login";

    public UserManager(Context c) {
        this.context = c;
    }

    public boolean isLogin() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(IS_LOGIN_KEY, false);
    }

    public void saveLoginStatus(boolean isLogin) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(IS_LOGIN_KEY, isLogin).apply();
    }
}
