/**
 * Created by osys on 2022/08/28 21:48.
 */
public class Demo4 {
    public static void main(String[] args) {
        // 有两把锁
        Test4.method1();
        Test4.method2();
    }
}

class Test4 {
    private static int data = 0;

    /** 同步代码块 */
    public static synchronized int method1() {
        return data++;
    }

    /** 同步代码块 */
    public static synchronized int method2() {
        return data--;
    }
}