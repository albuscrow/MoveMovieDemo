package ac.moviemoving.data;

import com.google.gson.Gson;

/**
 * Created by ac on 7/24/16.
 * todo some describe
 */
public class MyMessage {
    String[] receivers;
    String content;
    boolean[] isReceived;

    static MyMessage parse(String receiver, String content) {
        MyMessage mm = new MyMessage();
        mm.receivers = receiver.split(",");
        mm.content = content;
        mm.isReceived = new boolean[mm.receivers.length];
        for (int i = 0; i < mm.isReceived.length; ++i) {
            mm.isReceived[i] = false;
        }
        return mm;
    }

    public String toJsonString() {
        return new Gson().toJson(this);
    }
}
