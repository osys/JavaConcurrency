/**
 * Created by osys on 2022/08/28 21:48.
 */
public class Holder {
    private final int n;

    public Holder(int n) {
        this.n = n;
    }

    public void assertSanity() {
        if (n != n) {
            throw new AssertionError("This statement is false.");
        }
    }
}