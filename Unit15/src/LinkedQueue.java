import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by osys on 2022/08/28 21:48.
 */
@ThreadSafe
public class LinkedQueue <E> {

    private final Node<E> dummy = new Node<E>(null, null);
    private final AtomicReference<Node<E>> head = new AtomicReference<Node<E>>(dummy);
    private final AtomicReference<Node<E>> tail = new AtomicReference<Node<E>>(dummy);

    public boolean putHead(E item) {
        Node<E> newHead = new Node<E>(item, null);
        while (true) {
            // 链表头节点
            Node<E> curHead = head.get();
            // 保证 curHead 还是链表头节点
            if (curHead == head.get()) {
                // 向头部插入新节点
                if (newHead.next.compareAndSet(null, curHead)) {
                    // 头部节点向前移动一位
                    return head.compareAndSet(curHead, newHead);
                }
            }
        }
    }

    public boolean putTail(E item) {
        Node<E> newNode = new Node<E>(item, null);
        while (true) {
            // 链表尾节点
            Node<E> curTail = tail.get();
            // 链表尾节点指向下一个节点的指针
            Node<E> tailNext = curTail.next.get();
            // 保证前面获取的尾巴后面没有插入新的节点
            if (curTail == tail.get()) {
                // 前面获取的尾巴后面已经插入了新的节点
                if (tailNext != null) {
                    // 将尾巴向后移动一位
                    tail.compareAndSet(curTail, tailNext);
                // 前面获取的尾巴后面还未插入了新的节点，向尾巴后面插入新节点
                } else if (curTail.next.compareAndSet(null, newNode))  {
                    // 插入成功，将尾巴向后移动一位
                    return tail.compareAndSet(curTail, newNode);
                }
            }
        }
    }

    private static class Node <E> {
        final E item;
        final AtomicReference<Node<E>> next;

        public Node(E item, Node<E> next) {
            this.item = item;
            this.next = new AtomicReference<Node<E>>(next);
        }
    }
}
