package com.kristurek.polskatv.iptv.polskatelewizjausa.converter;

public interface Converter<T, R> {
    R convert(T bean);
}
