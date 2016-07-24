package ac.moviemoving.data;

/**
 * Created by ac on 7/24/16.
 * todo some describe
 */
public abstract class Action {
    public abstract void act();
    static void actIfNotNull(Action action) {
        if (action != null) {
            action.act();
        }
    }
}
