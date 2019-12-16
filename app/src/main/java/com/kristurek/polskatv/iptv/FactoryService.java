package com.kristurek.polskatv.iptv;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.IptvService;
import com.kristurek.polskatv.iptv.polbox.PolboxService;
import com.kristurek.polskatv.iptv.polbox.retrofit.PolboxApiFactory;
import com.kristurek.polskatv.iptv.polskatelewizjausa.PolskaTelewizjaUsaService;
import com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit.PolskaTelewizjaUsaApiFactory;
import com.kristurek.polskatv.iptv.util.Tag;

public enum FactoryService {

    SERVICE;

    private IptvService service;

    public void initializeService(ServiceProvider serviceProvider) {
        if (service != null)
            Log.d(Tag.API, "Service exists, reinitialize service[" + serviceProvider + "]");

        try {
            if (serviceProvider.equals(ServiceProvider.POLBOX))
                service = new PolboxService(PolboxApiFactory.create());
            else if (serviceProvider.equals(ServiceProvider.POLSKA_TELEWIZJA_USA))
                service = new PolskaTelewizjaUsaService(PolskaTelewizjaUsaApiFactory.create());
            else
                throw new IllegalArgumentException("Cannot initialize service, no impl[" + serviceProvider + "]");

            Log.d(Tag.API, "End initialize service[" + serviceProvider + "][" + service + "]");
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot initialize service because class name is wrong[" + serviceProvider + "]", e.getCause());
        }
    }

    public IptvService getInstance() {
        if (service == null)
            throw new IllegalStateException("Service not initialized");
        return service;
    }
}
