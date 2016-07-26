package ac.moviemoving.model;

import java.util.Random;

/**
 * Created by ac on 7/24/16.
 * todo some describe
 */
public class MyMovie {
    static String[] names = {"Movie A", "Movie B", "Movie C", "Movie D", "Movie E", "Movie F", "Movie G", "Movie H", "Movie I"};
    String name = "Movie Name";

    static private Random random = new Random(10086);
    static public MyMovie randomOne(){
        MyMovie mm = new MyMovie();
        mm.name = names[random.nextInt(9)];
        return mm;
    }
}
