/**
 * Created by osys on 2022/08/28 21:48.
 */
public class PseudoRandom {
    int calculateNext(int prev) {
        prev ^= prev << 6;
        prev ^= prev >>> 21;
        prev ^= (prev << 7);
        return prev;
    }
}