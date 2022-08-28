import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by osys on 2022/08/28 21:48.
 */
public class TimeBudget {

    private static ExecutorService exec = Executors.newCachedThreadPool();

    /**
     * 获得旅游行情排名
     * @param travelInfo 旅游资讯
     * @param companies 公司 ---- set集合
     * @param ranking 排行
     * @param time 超时时间
     * @param unit 时间单位
     */
    public List<TravelQuote> getRankedTravelQuotes(TravelInfo travelInfo,
                                                   Set<TravelCompany> companies,
                                                   Comparator<TravelQuote> ranking,
                                                   long time,
                                                   TimeUnit unit) throws InterruptedException {
        // 将【每个公司 + 旅游资讯】封装成 task
        List<QuoteTask> tasks = new ArrayList<>();
        for (TravelCompany company : companies) {
            tasks.add(new QuoteTask(company, travelInfo));
        }

        // 将所有 task 提交给 ExecutorService
        // 所有任务在规定时间内执行完任务
        List<Future<TravelQuote>> quoteFutures = exec.invokeAll(tasks, time, unit);

        // 报价获取
        List<TravelQuote> quotes = new ArrayList<>(tasks.size());
        Iterator<QuoteTask> taskIter = tasks.iterator();
        for (Future<TravelQuote> quoteFuture : quoteFutures) {
            QuoteTask task = taskIter.next();
            try {
                // 获取报价成功
                quotes.add(quoteFuture.get());
            } catch (ExecutionException e) {
                // 获取报价失败
                quotes.add(task.getFailureQuote(e.getCause()));
            } catch (CancellationException e) {
                // 获取报价超时
                quotes.add(task.getTimeoutQuote(e));
            }
        }

        Collections.sort(quotes, ranking);
        return quotes;
    }

}

class QuoteTask implements Callable<TravelQuote> {
    private final TravelCompany company;
    private final TravelInfo travelInfo;

    public QuoteTask(TravelCompany company, TravelInfo travelInfo) {
        this.company = company;
        this.travelInfo = travelInfo;
    }

    /** 获取报价失败 */
    TravelQuote getFailureQuote(Throwable t) {
        return null;
    }

    /** 获取报价超时 */
    TravelQuote getTimeoutQuote(CancellationException e) {
        return null;
    }

    @Override
    public TravelQuote call() throws Exception {
        // 获取旅游报价
        return company.solicitQuote(travelInfo);
    }
}

/** 旅游公司 */
interface TravelCompany {
    /** 获取旅游报价 */
    TravelQuote solicitQuote(TravelInfo travelInfo) throws Exception;
}

/** 旅游报价 */
interface TravelQuote {
}

/** 旅游资讯 */
interface TravelInfo {
}
