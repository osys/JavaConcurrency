import net.jcip.annotations.ThreadSafe;

import java.util.Vector;

/**
 * @author osys
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
