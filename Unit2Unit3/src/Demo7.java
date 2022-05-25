import java.util.Vector;

/**
 * @author osys
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
