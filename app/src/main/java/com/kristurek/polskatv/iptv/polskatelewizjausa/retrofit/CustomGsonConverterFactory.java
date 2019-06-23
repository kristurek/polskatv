package com.kristurek.polskatv.iptv.polskatelewizjausa.retrofit;

import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomGsonConverterFactory extends Converter.Factory {

    private GsonConverterFactory factory;

    public CustomGsonConverterFactory(GsonConverterFactory factory) {
        this.factory = factory;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(final Type type,
                                                            Annotation[] annotations, Retrofit retrofit) {
        Class<? extends BaseRetrofitResponse> targetClassType = findTargetClassType(annotations);
        if (targetClassType == null)
            return null;

        return factory.responseBodyConverter(targetClassType != null ? targetClassType : type, annotations, retrofit);
    }

    private Class<? extends BaseRetrofitResponse> findTargetClassType(Annotation[] annotations) {
        for (Annotation annotation : annotations)
            if (TargetClass.class == annotation.annotationType())
                return ((TargetClass) annotation).clazz();
        return null;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                          Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return factory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }

}
