import net.jcip.annotations.ThreadSafe;

/**
 * @author osys
 */
@ThreadSafe
public class Widget {
    public synchronized void doSomething() {
        System.out.println("父类，做某些事");
    }
}
