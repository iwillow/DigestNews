package com.iwillow.android.digestnews.util;

import com.iwillow.android.digestnews.entity.Source;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.observables.GroupedObservable;

/**
 * Created by https://www.github.com/iwillow on 2016/5/6.
 */
public class ReferenceUtil {

    public static Observable<Source> groupSource(List<Source> sources) {

        Observable<GroupedObservable<String, Source>> groupedSources = Observable
                .from(sources)
                .groupBy(new Func1<Source, String>() {
                    @Override
                    public String call(Source source) {
                        return source.getPublisher();
                    }
                });

        return Observable.concat(groupedSources);
    }
}
