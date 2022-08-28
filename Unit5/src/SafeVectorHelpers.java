import java.util.Vector;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class SafeVectorHelpers {

    private final Vector list;

    public SafeVectorHelpers(Vector list) {
        this.list = list;
    }

    public Object getLast() {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            return list.get(lastIndex);
        }
    }

    public void deleteLast() {
        synchronized (list) {
            int lastIndex = list.size() - 1;
            list.remove(lastIndex);
        }
    }
}
