import sun.nio.ch.Secrets;

import java.util.HashSet;
import java.util.Set;

/**
 * @author osys
 */
public class PublishObject {

    private static final Set<Secrets> publishSecrets = new HashSet<>();

    public static Set<Secrets> getInstance() {
        return publishSecrets;
    }
}
