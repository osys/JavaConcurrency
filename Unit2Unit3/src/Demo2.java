/**
 * Created by osys on 2022/08/28 21:48.
 */
public class Demo2 {
    private final int data = 0;

    public synchronized void synMethod() {
        int add = getData();
        doSomething(add);
    }

    public synchronized int getData() {
        return data;
    }

    public synchronized void doSomething(int data) {
        System.out.println("做某些事");
        System.out.println("data=" + data);
    }
}