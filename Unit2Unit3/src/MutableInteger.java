import net.jcip.annotations.NotThreadSafe;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@NotThreadSafe
public class MutableInteger {
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
