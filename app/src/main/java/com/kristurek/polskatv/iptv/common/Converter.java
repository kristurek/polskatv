package com.kristurek.polskatv.iptv.common;

public interface Converter<T, R> {
    R convert(T bean);
}
