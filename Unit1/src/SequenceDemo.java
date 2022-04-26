import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * @author osys
 */
public class SequenceDemo {
    public static void main(String[] args) {
        Sequence sequence = new Sequence(100);

        MeThread thread = new MeThread(sequence);
        Thread meThread1 = new Thread(thread, "线程1");
        Thread meThread2 = new Thread(thread, "线程2");
        Thread meThread3 = new Thread(thread, "线程3");

        meThread1.start();
        meThread2.start();
        meThread3.start();
    }
}

@ThreadSafe
class Sequence {
    @GuardedBy("this")
    private int value;

    public Sequence(int value) {
        this.value = value;
    }

    public synchronized int next() {
        return this.value++;
    }
}

class MeThread implements Runnable {

    private final Sequence sequence;

    public MeThread(Sequence sequence) {
        this.sequence = sequence;
    }

    @Override
    public void run() {
        // print
        System.out.println(Thread.currentThread().getName() + "------------------ " + sequence.next() + " ------------------");
    }
}