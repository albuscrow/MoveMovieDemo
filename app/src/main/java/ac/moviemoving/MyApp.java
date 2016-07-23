package ac.moviemoving;

import ac.moviemoving.manager.UserManager;
import android.app.Application;

/**
 * Created by ac on 7/23/16.
 * todo some describe
 */
public class MyApp extends Application {
    private UserManager userManager;
    private static MyApp INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        this.userManager = new UserManager(this);
        INSTANCE = this;
    }

    public static MyApp getInstance() {
        return INSTANCE;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
