import sun.nio.ch.Secrets;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class PublishObject {

    private static final Set<Secrets> publishSecrets = new HashSet<>();

    public static Set<Secrets> getInstance() {
        return publishSecrets;
    }
}
