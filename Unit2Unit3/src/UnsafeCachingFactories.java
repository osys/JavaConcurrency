import net.jcip.annotations.NotThreadSafe;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@NotThreadSafe
public class UnsafeCachingFactories extends GenericServlet implements Servlet {

    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();

    private final AtomicReference<BigInteger[]> lastFactor = new AtomicReference<>();

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {  }

    @Override
    public ServletConfig getServletConfig() {return null;}

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        BigInteger i = extractFromRequest(servletRequest);
        if (lastNumber.get().equals(i)) {
            encodeIntoResponse(servletResponse, lastFactor.get());
        } else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactor.set(factors);
            encodeIntoResponse(servletResponse, factors);
        }
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
