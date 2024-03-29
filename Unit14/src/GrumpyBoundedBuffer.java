import net.jcip.annotations.ThreadSafe;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@ThreadSafe
public class GrumpyBoundedBuffer <V> extends BaseBoundedBuffer<V> {

    public GrumpyBoundedBuffer() {
        this(100);
    }

    public GrumpyBoundedBuffer(int size) {
        super(size);
    }

    public synchronized void put(V v) throws BufferFullException {
        if (isFull()) {
            throw new BufferFullException();
        }
        doPut(v);
    }

    public synchronized V take() throws BufferEmptyException {
        if (isEmpty()) {
            throw new BufferEmptyException();
        }
        return doTake();
    }
}

class BufferFullException extends RuntimeException {
}

class BufferEmptyException extends RuntimeException {
}
