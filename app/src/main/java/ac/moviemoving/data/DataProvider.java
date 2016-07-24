package ac.moviemoving.data;

import ac.moviemoving.MyApp;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ac on 7/24/16.
 * todo some describe
 */
public class DataProvider {
    static boolean isLogining = false;
    static final private String REGISTER_USER = "RegisterUser";

    public static boolean isLogining() {
        return isLogining;
    }

    private static String mockUser = "17816861269 123456;";

    public static void login(String phoneNumber, String password, Action successAction, Action failAction) {
        isLogining = true;
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MyApp.getInstance().getCurrentActivity().runOnUiThread(() -> {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApp.getInstance());
                String users = sp.getString(REGISTER_USER, "") + mockUser;
                for (String s : users.split(";")) {
                    String[] userNameAndPassword = s.split(" ");
                    if (userNameAndPassword.length != 2) {
                        continue;
                    }
                    if (userNameAndPassword[0].equals(phoneNumber) && userNameAndPassword[1].equals(password)) {
                        Action.actIfNotNull(successAction);
                        isLogining = false;
                        return;
                    }
                }
                isLogining = false;
                Action.actIfNotNull(failAction);
            });
        }).start();
    }

    public static void register(String phoneNumber, String password, Action successAction, Action failAction) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApp.getInstance());
        String users = sp.getString(REGISTER_USER, "");
        for (String s : users.split(";")) {
            String[] userNameAndPassword = s.split(" ");
            if (userNameAndPassword.length != 2) {
                continue;
            }
            if (userNameAndPassword[0].equals(phoneNumber)) {
                Action.actIfNotNull(failAction);
                return;
            }
        }
        sp.edit().putString(REGISTER_USER, users + phoneNumber + " " + password + ";").apply();
        Action.actIfNotNull(successAction);
    }
}
