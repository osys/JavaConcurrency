import net.jcip.annotations.ThreadSafe;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author osys
 */
@ThreadSafe
public class CountingFactories implements Servlet {

    private final AtomicLong count = new AtomicLong(0);

    public long getCount() {
        return count.get();
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) { }

    public BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    public BigInteger[] factor(BigInteger i) {
        return new BigInteger[] { i };
    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        BigInteger[] factories = factor(i);
        count.incrementAndGet();
        encodeIntoResponse(servletResponse, factories);

        System.out.println("Input:" + servletRequest.getParameter("demo"));
        System.out.println("i=" + i.toString());
        System.out.println("factories=" + Arrays.toString(factories));
        System.out.println(count);
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
