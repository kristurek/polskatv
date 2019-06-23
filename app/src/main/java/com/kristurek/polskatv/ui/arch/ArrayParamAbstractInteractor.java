package com.kristurek.polskatv.ui.arch;

import android.util.Log;

import com.kristurek.polskatv.util.Tag;

import io.reactivex.Single;

public abstract class ArrayParamAbstractInteractor<R, P> {

    public Single<R> execute(P... param) {
        return Single.create(emitter -> {
            try {
                emitter.onSuccess(process(param));
            } catch (Exception e) {
                Log.e(Tag.SERVICE, e.getMessage(), e);
                emitter.onError(e);
            }
        });
    }

    protected abstract R process(P... param) throws Exception;
}
