import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@ThreadSafe
public class SynchronizedFactories implements Servlet {

    @GuardedBy("this")
    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();

    @GuardedBy("this")
    private final AtomicReference<BigInteger[]> lastFactor = new AtomicReference<>();

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {}

    @Override
    public ServletConfig getServletConfig() {return null;}

    @Override
    public synchronized void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        if (!lastNumber.get().equals(i)) {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactor.set(factors);
            encodeIntoResponse(servletResponse, factors);
        } else {
            encodeIntoResponse(servletResponse, lastFactor.get());
        }

        System.out.println(lastNumber.get() + "-----------------------");
        System.out.println(Arrays.toString(lastFactor.get()) + "-----------------------");
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        return new BigInteger[]{i};
    }

    @Override
    public String getServletInfo() {return null;}

    @Override
    public void destroy() {}
}
