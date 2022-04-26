import net.jcip.annotations.NotThreadSafe;

/**
 * @author osys
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
