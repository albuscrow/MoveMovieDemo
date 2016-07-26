package ac.moviemoving.data;

import ac.moviemoving.MyApp;
import ac.moviemoving.model.RoomSchedule;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

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

    public static List<RoomSchedule> getRoomSchedule() {
        List<RoomSchedule> RoomSchedules = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            RoomSchedules.add(new RoomSchedule());
        }
        return RoomSchedules;
    }

    static final String MESSAGES = "message";
    public static void sendMessage(String receiver, String content, String time) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApp.getInstance());
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(MESSAGES, sp.getString(MESSAGES, "") + ";" + MyMessage.parse(receiver, content, time).toJsonString());
        editor.apply();
    }

    public static List<MyMessage> getMessage() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApp.getInstance());
        String messagesStr = sp.getString(MESSAGES, "");
        if (messagesStr.length() == 0) {
            return new ArrayList<>();
        }
        System.out.println(messagesStr);
        List<MyMessage> messages = new ArrayList<>();
        for (String messageStr : messagesStr.split(";")) {
            MyMessage m = MyMessage.parseFromJson(messageStr);
            if (m != null) {
                messages.add(m);
            }
        }
        return messages;
    }

    public static void allMessageReaded() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MyApp.getInstance());
        String messagesStr = sp.getString(MESSAGES, "");
        if (messagesStr.length() == 0) {
            return;
        }
        List<MyMessage> messages = new ArrayList<>();
        for (String messageStr : messagesStr.split(";")) {
            MyMessage m = MyMessage.parseFromJson(messageStr);
            if (m != null) {
                messages.add(m);
            }
        }
        messagesStr = "";
        for (MyMessage m: messages) {
            m.allReaded();
            messagesStr += ";" + m.toJsonString();
        }

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(MESSAGES, messagesStr);
        editor.apply();
    }
}
