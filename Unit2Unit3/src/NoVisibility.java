/**
 * @author osys
 */
public class NoVisibility {
    /** 默认值为 false */
    private static boolean ready;
    /** 默认值为 0 */
    private static int number;

    private static class ReaderTread extends Thread {
        @Override
        public void run() {
            this.setName("Reader");
            while (!ready) {
                /*
                当一个线程使用了 Thread.yield() 方法之后，它就会把自己CPU执行的时间让掉，让自己或者其它的线程运行。

                打个比方：
                    现在有很多人在排队上厕所，好不容易轮到这个人上厕所了，突然这个人说：“我要和大家来个竞赛，看谁先抢到厕所！”。
                    然后所有的人在同一起跑线冲向厕所，有可能是别人抢到了，也有可能他自己有抢到了。
                    我们还知道线程有个优先级的问题，那么手里有优先权的这些人就一定能抢到厕所的位置吗?
                    不一定的，他们只是概率上大些，也有可能没特权的抢到了。
                 */
                Thread.yield();
            }
            System.out.println(number);
        }
    }

    // main 线程
    public static void main(String[] args) {
        // Reader 线程
        new ReaderTread().start();
        number = 66;
        ready = true;
    }
}
