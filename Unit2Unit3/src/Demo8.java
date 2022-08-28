/**
 * Created by osys on 2022/08/28 21:48.
 */
public class Demo8 {
    /** 状态的标志 */
    private volatile boolean aSleep;

    public void mainMethod() {
        startDoSomething();
        while (!aSleep) {
            stopDoSomething();
        }
    }

    public void startDoSomething() {
        System.out.println("开始 ------ 做某些事");
    }

    public void stopDoSomething() {
        System.out.println("停止 ------ 做某些事");
    }
}
