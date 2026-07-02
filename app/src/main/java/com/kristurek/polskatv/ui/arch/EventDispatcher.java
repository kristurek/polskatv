package com.kristurek.polskatv.ui.arch;

import org.greenrobot.eventbus.EventBus;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventDispatcher {

    @Inject
    public EventDispatcher() {
    }

    public void post(Event event) {
        EventBus.getDefault().post(event);
    }
}
