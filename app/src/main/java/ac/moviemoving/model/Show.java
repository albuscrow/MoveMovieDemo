package ac.moviemoving.model;

import ac.CalUtil;

/**
 * Created by ac on 7/24/16.
 * todo some describe
 */
public class Show {
    private static int next = 0;
    String startTime;
    String endTime;
    MyMovie move;

    public static Show randomShow() {
        Show s = new Show();
        s.move = MyMovie.randomOne();
        CalUtil calUtil = new CalUtil();
        s.startTime = calUtil.getTimeAfterNHours("yyyy-MM-dd HH:mm", 24 + next * 3);
        s.endTime = calUtil.getTimeAfterNHours("yyyy-MM-dd HH:mm", 24 + next * 3 + 2);
        ++ next;
        return s;
    }

    public static void initDate() {
        next = 0;
    }

    public static int getNext() {
        return next;
    }

    public static void setNext(int next) {
        Show.next = next;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public MyMovie getMove() {
        return move;
    }

    public void setMove(MyMovie move) {
        this.move = move;
    }
}
