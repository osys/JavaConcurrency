import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author osys
 */
public class Demo9 {
    public final Map<String, Date> lastLogin;

    public Demo9() {
        Map<String, Date> stringDate = new HashMap<>();
        stringDate.put("LeeHua", new Date());
        stringDate.put("Rainbow", new Date());
        lastLogin = Collections.synchronizedMap(stringDate);
    }
}
