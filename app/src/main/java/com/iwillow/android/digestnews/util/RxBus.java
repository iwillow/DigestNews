package com.iwillow.android.digestnews.util;

import rx.Observable;
import rx.functions.Action0;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Implementing an Event Bus With RxJava
 * <p/>
 * Created by http://nerds.weddingpartyapp.com/tech/2014/12/24/implementing-an-event-bus-with-rxjava-rxbus/  and
 * Created by http://www.jianshu.com/p/ca090f6e2fe2/
 */
public class RxBus {

    private static volatile RxBus defaultInstance;
    private final Subject _bus;

    public RxBus() {
        _bus = new SerializedSubject<>(PublishSubject.create());
    }

    public static RxBus getInstance() {
        RxBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    public void post(Object o) {
       /* try {
            bus.onNext(o);
            bus.onCompleted();
        } catch (Exception e) {
            bus.onError(e);
        }
*/
        _bus.onNext(o);
    }

    public <T> Observable<T> toObserverable(Class<T> eventType) {
        return _bus.ofType(eventType);
    }

    public boolean hasObservers() {
        return _bus.hasObservers();
    }
}
