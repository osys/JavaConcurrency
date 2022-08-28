/**
 * Created by osys on 2022/08/28 21:48.
 */
public class ThisEscape {
    public ThisEscape(EventSource source) {
        source.registerListener(new EventListener() {
            @Override
            public void onEvent(Event event) {
                doSomething(event);
            }
        });
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