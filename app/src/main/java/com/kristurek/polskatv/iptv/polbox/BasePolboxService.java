package com.kristurek.polskatv.iptv.polbox;

import android.util.Log;

import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.common.ExceptionHelper;
import com.kristurek.polskatv.iptv.core.exception.IptvConverterException;
import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.iptv.core.exception.common.ExceptionModel;
import com.kristurek.polskatv.iptv.polbox.pojo.common.BaseRetrofitResponse;
import com.kristurek.polskatv.iptv.polbox.pojo.error.ErrorRetrofitResponse;
import com.kristurek.polskatv.iptv.util.Tag;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Response;

public abstract class BasePolboxService {

    private static final String ANOTHER_CLIENT_WAS_LOGGED = "11";

    protected static <R> R process(Converter converter, Callable<Call<BaseRetrofitResponse>> callableService, Callable<?> autoReLoginService) throws IptvException {
        try {
            return process(converter, callableService);
        } catch (IptvException e) {
            Log.d(Tag.API, e.getMessage());
            if (e.getException() != null && (e.getException().getCode().equals(ANOTHER_CLIENT_WAS_LOGGED))) {
                tryReLoginProcess(autoReLoginService);
                return process(converter, callableService);
            } else
                throw e;
        } catch (Exception e) {
            throw new IptvException(e.getMessage() != null ? e.getMessage() : ExceptionHelper.UNKNOWN_MSG, e);
        }
    }

    private static void tryReLoginProcess(Callable<?> callableService) throws IptvException {
        try {
            Log.d(Tag.API, "BasePolboxService.reLoginProcess()");

            callableService.call();

        } catch (IptvException e) {
            Log.e(Tag.API, "BasePolboxService.reLoginProcess()[Ignored exception]", e);
        } catch (Exception e) {
            Log.e(Tag.API, "BasePolboxService.reLoginProcess()[Ignored exception]", e);
        }
    }

    protected static <R> R process(Converter converter, Callable<Call<BaseRetrofitResponse>> callableService) throws IptvException {
        try {
            Call<BaseRetrofitResponse> call = callableService.call();
            Response<BaseRetrofitResponse> response = call.execute();

            Log.d(Tag.API, "BasePolboxService.process(){" +
                    "resopnse.toString[" + response + "], " +
                    "response.body()[" + response.body() + "], " +
                    "response.errorBody()[" + response.errorBody() + "], " +
                    "response.code()[" + response.code() + "]}");

            if (!response.isSuccessful())
                throw prepareException(response);

            if (response.body() instanceof ErrorRetrofitResponse) {
                throw prepareException((ErrorRetrofitResponse) response.body());
            }
            return convert(response.body(), converter);
        } catch (IptvException e) {
            Log.e(Tag.API, "BasePolboxService.process()", e);
            throw e;
        } catch (Exception e) {
            Log.e(Tag.API, "BasePolboxService.process()", e);
            throw new IptvException(e.getMessage() != null ? e.getMessage() : ExceptionHelper.UNKNOWN_MSG, e);
        }
    }

    private static <R> R convert(BaseRetrofitResponse response, Converter converter) throws IptvConverterException {
        try {
            return (R) converter.convert(response);
        } catch (Exception e) {
            Log.e(Tag.API, "BasePolboxService.convert(" + response + ", " + converter + ")", e);
            throw new IptvConverterException(e.getMessage() != null ? e.getMessage() : ExceptionHelper.CONVERTER_MSG, e);
        }
    }

    private static IptvException prepareException(ErrorRetrofitResponse response) {
        return new IptvException(new ExceptionModel(response.getError().getMessage(), response.getError().getCode()));
    }

    private static IptvException prepareException(Response<BaseRetrofitResponse> response) {
        return new IptvException(new ExceptionModel(response.errorBody().toString(), String.valueOf(response.code())));
    }
}
