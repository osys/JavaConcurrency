import net.jcip.annotations.NotThreadSafe;

/**
 * @author osys
 */
public class LazyInitRaceDemo {
    private static final LazyInitRace lazyInitRace = new LazyInitRace();
    private static final MyThread thread = new MyThread(lazyInitRace);

    public static void main(String[] args) {
        // 创建两个线程来调用 setInstance 方法
        Thread myThread1 = new Thread(thread, "线程1");
        Thread myThread2 = new Thread(thread, "线程2");
        myThread1.start();
        myThread2.start();
    }

    static class MyThread implements Runnable {

        private final LazyInitRace lazyInitRace;

        public MyThread(LazyInitRace lazyInitRace) {
            this.lazyInitRace = lazyInitRace;
        }

        @Override
        public void run() {
            // 如果 ExpensiveObject == null，那么创建 ExpensiveObject
            if (lazyInitRace.getInstance() == null) {
                lazyInitRace.setInstance(new ExpensiveObject());
            }
        }
    }

}

@NotThreadSafe
class LazyInitRace {

    private final ExpensiveObject instance = null;

    public ExpensiveObject getInstance() {
        return instance;
    }

    public void setInstance(ExpensiveObject instance) {
        instance = instance;
    }
}

class ExpensiveObject {
    public ExpensiveObject() {
        System.out.println(Thread.currentThread().getName() + "----------------------- 创建一个 ExpensiveObject 对象");
    }
}
