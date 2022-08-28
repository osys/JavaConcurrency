import net.jcip.annotations.NotThreadSafe;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@NotThreadSafe
public class UnsafeLazyInitialization {
    private static Resource resource;

    public static Resource getInstance() {
        if (resource == null) {
            resource = new Resource(); // 不安全的发布
        }
        return resource;
    }

    static class Resource {
    }
}
