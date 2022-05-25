import net.jcip.annotations.ThreadSafe;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author osys
 */
@ThreadSafe
public class ImprovedList<T> implements List<T> {
    private final List<T> list;

    /**
     * PRE: list 参数是线程安全的。
     */
    public ImprovedList(List<T> list) {this.list = list;}

    public synchronized boolean putIfAbsent(T x) {
        boolean contains = list.contains(x);
        if (contains) {
            list.add(x);
        }
        return !contains;
    }

    // List 方法的普通委托。
    // 可变方法必须同步以确保 putIfAbsent 的原子性。
    @Override
    public synchronized void clear() {list.clear();}
    @Override
    public synchronized void add(int index, T element) {list.add(index, element);}
    @Override
    public synchronized boolean add(T e) {return list.add(e);}
    @Override
    public synchronized boolean remove(Object o) {return list.remove(o);}
    @Override
    public synchronized boolean addAll(Collection<? extends T> c) {return list.addAll(c);}
    @Override
    public synchronized boolean addAll(int index, Collection<? extends T> c) {return list.addAll(index, c);}
    @Override
    public synchronized boolean removeAll(Collection<?> c) {return list.removeAll(c);}
    @Override
    public synchronized boolean retainAll(Collection<?> c) {return list.retainAll(c);}
    @Override
    public synchronized T set(int index, T element) {return list.set(index, element);}
    @Override
    public synchronized T remove(int index) {return list.remove(index);}

    // 不可变方法
    @Override
    public  T get(int index) {return list.get(index);}
    @Override
    public int size() {return list.size();}
    @Override
    public boolean isEmpty() {return list.isEmpty();}
    @Override
    public boolean contains(Object o) {return list.contains(o);}
    @Override
    public Iterator<T> iterator() {return list.iterator();}
    @Override
    public Object[] toArray() {return list.toArray();}
    @Override
    public <T> T[] toArray(T[] a) {return list.toArray(a);}
    @Override
    public boolean containsAll(Collection<?> c) {return list.containsAll(c);}
    @Override
    public boolean equals(Object o) {return list.equals(o);}
    @Override
    public int hashCode() {return list.hashCode();}
    @Override
    public int indexOf(Object o) {return list.indexOf(o);}
    @Override
    public int lastIndexOf(Object o) {return list.lastIndexOf(o);}
    @Override
    public ListIterator<T> listIterator() {return list.listIterator();}
    @Override
    public ListIterator<T> listIterator(int index) {return list.listIterator(index);}
    @Override
    public List<T> subList(int fromIndex, int toIndex) {return list.subList(fromIndex, toIndex);}
}