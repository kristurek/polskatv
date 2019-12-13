package com.kristurek.polskatv.iptv.polskatelewizjausa;

import android.util.Log;

import com.kristurek.polskatv.iptv.core.exception.IptvException;
import com.kristurek.polskatv.iptv.core.exception.IptvConverterException;
import com.kristurek.polskatv.iptv.core.exception.common.ExceptionModel;
import com.kristurek.polskatv.iptv.common.Converter;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.error.ErrorRetrofitResponse;
import com.kristurek.polskatv.iptv.common.ExceptionHelper;
import com.kristurek.polskatv.iptv.util.Tag;

import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Response;

public abstract class BasePolskaTelewizjaUsaService {

    private static final String SESSION_TIME_OUT = "STIMEOUT";
    private static final String NO_SUCH_SESSION = "NO_SUCH_SESSION";
    private static final String WRONG_SID = "WRONG_SID";

    protected static <R> R process(Converter converter, Callable<Call<BaseRetrofitResponse>> callableService, Callable<?> autoReLoginService) throws IptvException {
        try {
            return process(converter, callableService);
        } catch (IptvException e) {
            if (e.getException() != null && (e.getException().getCode().equals(SESSION_TIME_OUT) || e.getException().getCode().equals(NO_SUCH_SESSION) || e.getException().getCode().equals(WRONG_SID))) {
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
            Log.d(Tag.API, "BasePolskaTelewizjaUsaService.reLoginProcess()");

            callableService.call();

        } catch (IptvException e) {
            Log.e(Tag.API, "BasePolskaTelewizjaUsaService.reLoginProcess()[Ignored exception]", e);
        } catch (Exception e) {
            Log.e(Tag.API, "BasePolskaTelewizjaUsaService.reLoginProcess()[Ignored exception]", e);
        }
    }

    protected static <R> R process(Converter converter, Callable<Call<BaseRetrofitResponse>> callableService) throws IptvException {
        try {
            Call<BaseRetrofitResponse> call = callableService.call();
            Response<BaseRetrofitResponse> response = call.execute();

            Log.d(Tag.API, "BasePolskaTelewizjaUsaService.process(){" +
                    "resopnse.toString[" + response + "], " +
                    "response.body()[" + response.body() + "], " +
                    "response.errorBody()[" + response.errorBody() + "], " +
                    "response.code()[" + response.code() + "]}");

            if (!response.isSuccessful())
                throw prepareException(response);

            if (response.body() instanceof ErrorRetrofitResponse)
                throw prepareException((ErrorRetrofitResponse) response.body());

            return convert(response.body(), converter);
        } catch (IptvException e) {
            Log.e(Tag.API, "BasePolskaTelewizjaUsaService.process()", e);
            throw e;
        } catch (Exception e) {
            Log.e(Tag.API, "BasePolskaTelewizjaUsaService.process()", e);
            throw new IptvException(e.getMessage() != null ? e.getMessage() : ExceptionHelper.UNKNOWN_MSG, e);
        }
    }

    private static <R> R convert(BaseRetrofitResponse response, Converter converter) throws IptvConverterException {
        try {
            return (R) converter.convert(response);
        } catch (Exception e) {
            Log.e(Tag.API, "BasePolskaTelewizjaUsaService.convert(" + response + ", " + converter + ")", e);
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
