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

    public String[] getReceivers() {
        return receivers;
    }

    public void setReceivers(String[] receivers) {
        this.receivers = receivers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public boolean[] getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(boolean[] isReceived) {
        this.isReceived = isReceived;
    }

    public String getReceiversStr() {
        String res = "";
        for (String s : receivers) {
            res += s + ",";
        }
        return res.substring(0, res.length() - 1);
    }

    public String getUnReceiversStr() {
        String res = "";
        for (int i = 0; i < receivers.length; ++i) {
            String s = receivers[i];
            boolean isReceive = isReceived[i];
            if (!isReceive) {
                res += s + ",";
            }
        }
        if (res.isEmpty()) {
            return "All Read";
        } else {
            return res.substring(0, res.length() - 1);
        }
    }

    public void allReaded() {
        for (int i = 0; i < isReceived.length; ++i) {
            isReceived[i] = true;
        }
    }
}
