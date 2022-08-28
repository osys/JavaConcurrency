import net.jcip.annotations.ThreadSafe;

import java.util.Vector;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@ThreadSafe
public class BetterVector <E> extends Vector<E> {
    /** 扩展可序列化类时，应重新定义 serialVersionUID */
    static final long serialVersionUID = -3963416950630760754L;

    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !contains(x);
        if (absent) {
            add(x);
        }
        return absent;
    }
}
