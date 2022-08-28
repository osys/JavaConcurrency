import apple.laf.JRSUIConstants;
import net.jcip.annotations.GuardedBy;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class PrivateLock {

    private final Object myLock = new Object();

    @GuardedBy("myLock")
    JRSUIConstants.Widget widget;

    void someMethod() {
        synchronized (myLock) {
            // 访问或修改 widget 的状态
        }
    }
}
