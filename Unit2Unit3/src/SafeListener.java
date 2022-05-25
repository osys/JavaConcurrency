/**
 * @author osys
 */
public class SafeListener {
    private final EventListener listener;

    private SafeListener() {
        listener = new EventListener() {
            @Override
            public void onEvent(Event event) {
                doSomething(event);
            }
        };
    }

    public static SafeListener newInstance(EventSource source) {
        SafeListener safeListener = new SafeListener();
        source.registerListener(safeListener.listener);
        return safeListener;
    }

    void doSomething(Event event) {
    }

    /** 事件源 */
    interface EventSource {
        /**
         * 注册事件监听
         * @param eventListener 事件监听
         */
        void registerListener(EventListener eventListener);
    }

    /** 事件监听 */
    interface EventListener {
        /**
         * 一个事件
         * @param event 一个事件
         */
        void onEvent(Event event);
    }

    /** 事件 */
    interface Event {
    }
}
