package com.kristurek.polskatv.ui.arch;

import android.util.Log;

import com.kristurek.polskatv.util.Tag;

import io.reactivex.Single;

public abstract class VoidParamAbstractInteractor<R> {

    public Single<R> execute() {
        return Single.create(emitter -> {
            try {
                emitter.onSuccess(process());
            } catch (Exception e) {
                Log.e(Tag.SERVICE, e.getMessage(), e);
                emitter.onError(e);
            }
        });
    }

    protected abstract R process() throws Exception;
}

