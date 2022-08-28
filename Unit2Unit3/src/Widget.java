import net.jcip.annotations.ThreadSafe;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@ThreadSafe
public class Widget {
    public synchronized void doSomething() {
        System.out.println("父类，做某些事");
    }
}
