import java.util.Vector;

/**
 * @author osys
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
