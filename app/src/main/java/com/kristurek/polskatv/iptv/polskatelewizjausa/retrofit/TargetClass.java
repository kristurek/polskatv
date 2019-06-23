package com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit;

import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetClass {

    static class None extends BaseRetrofitResponse {
        private static final long serialVersionUID = 1L;

        private None() {
        }
    }

    Class<? extends BaseRetrofitResponse> clazz() default None.class;
}
