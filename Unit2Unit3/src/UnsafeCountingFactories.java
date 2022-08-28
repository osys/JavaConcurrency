import net.jcip.annotations.NotThreadSafe;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@NotThreadSafe
public class UnsafeCountingFactories implements Servlet {

    private long count = 0;

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
        ++count;
        encodeIntoResponse(servletResponse, factories);

        System.out.println("Input:" + servletRequest.getParameter("demo"));
        System.out.println("i=" + i.toString());
        System.out.println("factories=" + Arrays.toString(factories));
        System.out.println(count);
    }

    public long getCount() {
        return count;
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) { }

    public BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    public BigInteger[] factor(BigInteger i) {
        return new BigInteger[] { i };
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
