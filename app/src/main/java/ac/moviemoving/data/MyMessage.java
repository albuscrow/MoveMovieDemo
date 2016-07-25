package ac.moviemoving.data;

import ac.CalUtil;
import com.google.gson.Gson;

/**
 * Created by ac on 7/24/16.
 * todo some describe
 */
public class MyMessage {
    String[] receivers;
    String content;
    String alertTime;
    String sendTime;
    boolean[] isReceived;

    static MyMessage parse(String receiver, String content, String time) {
        MyMessage mm = new MyMessage();
        mm.receivers = receiver.split(",");
        mm.content = content;
        mm.alertTime = time;
        mm.sendTime = new CalUtil().getNowTime("yyyy-MM-dd HH:mm:ss");
        mm.isReceived = new boolean[mm.receivers.length];
        for (int i = 0; i < mm.isReceived.length; ++i) {
            mm.isReceived[i] = true;
        }
        mm.isReceived[0] = false;
        return mm;
    }

    public String toJsonString() {
        return new Gson().toJson(this);
    }

    public static MyMessage parseFromJson(String messagesStr) {
        try {
            return new Gson().fromJson(messagesStr, MyMessage.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
