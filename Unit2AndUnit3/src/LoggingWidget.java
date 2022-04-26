import net.jcip.annotations.ThreadSafe;

/**
 * @author osys
 */
@ThreadSafe
public class LoggingWidget extends Widget {
    @Override
    public synchronized void doSomething() {
        System.out.println("子类，做某些事");
        super.doSomething();
    }
}
