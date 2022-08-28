import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@ThreadSafe
public class SafeStates {
    private final Map<String, String> states;

    public SafeStates() {
        states = new HashMap<String, String>();
        states.put("a", "A");
        states.put("b", "B");
        states.put("c", "C");
        /* put ...*/
    }

    public String getAbbreviation(String s) {
        return states.get(s);
    }
}
