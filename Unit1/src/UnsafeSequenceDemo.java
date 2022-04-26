import net.jcip.annotations.NotThreadSafe;

/**
 * @author osys
 */
public class UnsafeSequenceDemo {
    public static void main(String[] args) {
        UnsafeSequence unsafeSequence = new UnsafeSequence(100);

        MyThread thread = new MyThread(unsafeSequence);
        Thread myThread1 = new Thread(thread, "线程1");
        Thread myThread2 = new Thread(thread, "线程2");
        Thread myThread3 = new Thread(thread, "线程3");

        myThread1.start();
        myThread2.start();
        myThread3.start();
    }
}

@NotThreadSafe
class UnsafeSequence {
    private int value;

    public UnsafeSequence(int value) {
        this.value = value;
    }

    public int next() {
        return this.value++;
    }
}

class MyThread implements Runnable {

    private final UnsafeSequence unsafeSequence;

    public MyThread(UnsafeSequence unsafeSequence) {
        this.unsafeSequence = unsafeSequence;
    }

    @Override
    public void run() {
        // print
        System.out.println(Thread.currentThread().getName() + "------------------ " + unsafeSequence.next() + " ------------------");
    }
}