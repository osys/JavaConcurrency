/** Throwable 强制转换为 RuntimeException */
public class LaunderThrowable {

    /**
     * 将未经检查的 Throwable 抛出。
     *
     * 如果 throwable 是 RuntimeException 则返回 Throwable。
     * 如果 throwable 是 Error 则抛出 Error。
     * 否者抛出 IllegalStateException。
     */
    public static RuntimeException launderThrowable(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        } else if (throwable instanceof Error) {
            throw (Error) throwable;
        } else {
            throw new IllegalStateException("Not unchecked", throwable);
        }
    }
}
