import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@ThreadSafe
public class CachedFactories implements Servlet {
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {}

    @Override
    public ServletConfig getServletConfig() {return null;}

    @Override
    public String getServletInfo() {return null;}

    @Override
    public void destroy() {}

    @GuardedBy("this")
    private BigInteger lastNumber;

    @GuardedBy("this")
    private BigInteger[] lastFactors;

    /** 命中计数器 */
    @GuardedBy("this")
    private long hits;

    /** 缓存命中计数器 */
    @GuardedBy("this")
    private long cacheHits;

    /** 先检查再执行 */
    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        BigInteger[] factors = null;

        // 先检查再执行
        synchronized (this) {
            ++hits;                         // 命中计数器
            if (i.equals(lastNumber)) {
                // 判断 BigInteger 是否存在
                ++cacheHits;                // 缓存命中计数器
                factors = lastFactors.clone();
            }
        }

        // 确保对缓存的数值和因数分解进行同步更新（类似一个初始化）
        if (factors == null) {
            factors = factor(i);
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.clone();
            }
        }

        // 处理响应
        encodeIntoResponse(servletResponse, factors);
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    /** 从 Request 中获取 BigInteger */
    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        return new BigInteger[]{i};
    }
}
