import net.jcip.annotations.Immutable;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * 不可变容器，混存值
 * Created by osys on 2022/08/28 21:48.
 */
@Immutable
public class OneValueCache {
    private final BigInteger lastNumber;
    private final BigInteger[] lastFactors;

    public OneValueCache(BigInteger i,
                         BigInteger[] factors) {
        lastNumber = i;
        // 防止空指针异常
        if (factors == null) {
            lastFactors = null;
        } else {
            lastFactors = Arrays.copyOf(factors, factors.length);
        }
    }

    public BigInteger[] getFactors(BigInteger i) {
        if (lastNumber == null || !lastNumber.equals(i)) {
            // lastNumber = null 或 lastNumber != i
            return null;
        } else {
            return Arrays.copyOf(lastFactors, lastFactors.length);
        }
    }
}
