package ac.moviemoving;

/**
 * Created by ac on 7/23/16.
 * todo some describe
 */
public class Util {

    static public boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    static public boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    static public boolean isPhoneValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() == 11;
    }

}
