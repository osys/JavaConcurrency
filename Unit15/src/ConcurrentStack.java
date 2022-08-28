import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/** 使用链表实现栈 */
@ThreadSafe
public class ConcurrentStack<E> {
    AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();

    public void push(E item) {
        Node<E> newHead = new Node<E>(item);
        Node<E> oldHead;
        boolean cas = false;
        do {
            // 当前栈顶
            oldHead = top.get();
            // 新栈顶
            newHead.next = oldHead;
            // 比较并交换
            // 如果当前值(top.get()) == 期望值(oldHead)，则自动将值设置为给定的更新值(newHead)。
            cas = top.compareAndSet(oldHead, newHead);
        } while (!cas);
    }

    public E pop() {
        Node<E> oldHead;
        Node<E> newHead;
        boolean cas = false;
        do {
            oldHead = top.get();
            if (oldHead == null) {
                return null;
            }
            newHead = oldHead.next;
            // 比较并交换
            // 如果当前值(top.get()) == 期望值(oldHead)，则自动将值设置为给定的更新值(newHead)。
            cas = top.compareAndSet(oldHead, newHead);
        } while (!cas);
        return oldHead.item;
    }

    /** 链表 */
    private static class Node<E> {
        public final E item;
        public Node<E> next;

        public Node(E item) {
            this.item = item;
        }
    }
}
