import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@ThreadSafe
public class BetterAttributeStore {
    @GuardedBy("this")
    private final Map<String, String> attributes = new HashMap<>();

    public boolean userLocationMatches(String name, String regexp) {
        String key = "users." + name + ".location";
        String location;
        synchronized (this) {
            location = attributes.get(key);
        }
        if (location == null) {
            return false;
        } else {
            return Pattern.matches(regexp, location);
        }
    }
}
