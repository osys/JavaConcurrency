import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class ListHelper {
    @NotThreadSafe
    class BadListHelper <E> {
        public List<E> list = Collections.synchronizedList(new ArrayList<E>());

        public synchronized boolean putIfAbsent(E x) {
            boolean absent = !list.contains(x);
            if (absent) {
                list.add(x);
            }
            return absent;
        }

        // =========================
        // 等等有关操作 list 的其它方法
        // =========================
    }

    @ThreadSafe
    class GoodListHelper <E> {
        public List<E> list = Collections.synchronizedList(new ArrayList<E>());

        public boolean putIfAbsent(E x) {
            synchronized (list) {
                boolean absent = !list.contains(x);
                if (absent) {
                    list.add(x);
                }
                return absent;
            }
        }

        // =========================
        // 等等有关操作 list 的其它方法
        // =========================
    }
}
