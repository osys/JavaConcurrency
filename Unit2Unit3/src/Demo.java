/**
 * Created by osys on 2022/08/28 21:48.
 */
public class Demo {
    private int data = 0;

    /** 非原子操作 */
    public void synMethod() {
        int add = dataAdd();
        System.out.println("做某些事");
    }

    /** 同步代码块 */
    public synchronized int dataAdd() {
        return data++;
    }
}