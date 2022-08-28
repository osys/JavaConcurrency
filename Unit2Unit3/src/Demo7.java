import java.util.Vector;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class Demo7 {

    private Vector<String> vector = new Vector<>();

    public Demo7(Vector<String> vector) {
        this.vector = vector;
    }

    public void addElement(String element) {
        if (!vector.contains(element)) {
            vector.add(element);
        }
    }
}
