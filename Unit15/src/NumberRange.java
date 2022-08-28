import java.util.concurrent.atomic.AtomicInteger;

public class NumberRange {
    /** 不变约束: lower <= upper */
    private final AtomicInteger lower = new AtomicInteger(0);
    private final AtomicInteger upper = new AtomicInteger(0);

    /** 设置最低 number */
    public void setLower(int i) {
        // 警告 -- 不安全的 "检查再运行"
        if (i > upper.get()) {
            throw new IllegalArgumentException("can't set lower to " + i + " > upper");
        }
        lower.set(i);
    }

    /** 设置最高 number */
    public void setUpper(int i) {
        // 警告 -- 不安全的 "检查再运行"
        if (i < lower.get()) {
            throw new IllegalArgumentException("can't set upper to " + i + " < lower");
        }
        upper.set(i);
    }

    /**
     * 判断 i 是否满足 lower <= i <= upper
     * @return true or false
     */
    public boolean isInRange(int i) {
        return (i >= lower.get() && i <= upper.get());
    }
}