
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.common.BaseRetrofitResponse;

public class ErrorRetrofitResponse extends BaseRetrofitResponse {

    @SerializedName("error")
    @Expose
    private Error_ error;

    public Error_ getError() {
        return error;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorRetrofitResponse{");
        sb.append("error=").append(error);
        sb.append(", ").append(super.toString());
        sb.append('}');

        return sb.toString();
    }
}
