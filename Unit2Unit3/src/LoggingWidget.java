import net.jcip.annotations.ThreadSafe;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@ThreadSafe
public class LoggingWidget extends Widget {
    @Override
    public synchronized void doSomething() {
        System.out.println("子类，做某些事");
        super.doSomething();
    }
}
