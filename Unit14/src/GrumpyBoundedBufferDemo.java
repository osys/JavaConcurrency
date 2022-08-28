/**
 * Created by osys on 2022/08/28 21:48.
 */
public class GrumpyBoundedBufferDemo {
    private GrumpyBoundedBuffer<String> buffer;
    int SLEEP_GRANULARITY = 50;

    public void useBuffer() throws InterruptedException {
        while (true) {
            try {
                String item = buffer.take();
                // ... ...
                break;
            } catch (BufferEmptyException e) {
                Thread.sleep(SLEEP_GRANULARITY);
                Thread.yield();
            }
        }
    }
}
