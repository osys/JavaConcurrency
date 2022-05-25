import java.util.concurrent.atomic.AtomicReference;

/**
 * NumberRange2 类完整地保护它的不变约束
 * @author osys
 */
public class NumberRange2 {
    /** 不变约束: lower <= upper */
    private final AtomicReference<RangeNum> lowerAndUpper = new AtomicReference<>(new RangeNum(0, 0));

    /** 设置最低 number */
    public void setLower(int i) {
        lowerAndUpper.set(new RangeNum(i, lowerAndUpper.get().upper));
    }

    /** 设置最高 number */
    public void setUpper(int i) {
        lowerAndUpper.set(new RangeNum(lowerAndUpper.get().lower, i));
    }

    /**
     * 判断 i 是否满足 lower <= i <= upper
     * @return true or false
     */
    public boolean isInRange(int i) {
        return (i >= lowerAndUpper.get().lower && i <= lowerAndUpper.get().upper);
    }

    class RangeNum {
        public int lower;
        public int upper;
        public RangeNum(int lower, int upper) {
            // 警告 -- 不安全的 "检查再运行"
            if (upper < lower) {throw new IllegalArgumentException("upper must be greater than or equal to lower!");}
            this.lower = lower;
            this.upper = upper;
        }
    }
}

