/**
 * Created by osys on 2022/08/28 21:48.
 */
public class Demo3 {
    public static void main(String[] args) {
        // test3 对象，有两把锁
        Test3 test3 = new Test3();
        test3.method1();
        test3.method2();
    }
}

class Test3 {
    private int data = 0;

    /** 同步代码块 */
    public synchronized int method1() {
        return data++;
    }

    /** 同步代码块 */
    public synchronized int method2() {
        return data--;
    }
}