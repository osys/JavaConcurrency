/**
 * Created by osys on 2022/08/28 21:48.
 */
public class PossibleReordering {

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100000; i++) {
            new Thread(new RunnableTask()).start();
        }
    }

    private static class RunnableTask implements Runnable {

        int x = 0, y = 0;
        int a = 0, b = 0;

        @Override
        public void run() {
            Thread one = new Thread(new Runnable() {
                public void run() {
                    a = 1;
                    x = b;
                }
            });
            Thread other = new Thread(new Runnable() {
                public void run() {
                    b = 1;
                    y = a;
                }
            });
            one.start();
            other.start();
            try {
                one.join();
                other.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("(" + x + "," + y + ")");
        }
    }

}
