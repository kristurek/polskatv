
package com.kristurek.polskatv.iptv.polbox.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Settings {

    @SerializedName("bitrate")
    @Expose
    private Bitrate bitrate;
    @SerializedName("http_caching")
    @Expose
    private HttpCaching httpCaching;
    @SerializedName("stream_server")
    @Expose
    private StreamServer streamServer;
    @SerializedName("timeshift")
    @Expose
    private Timeshift timeshift;
    @SerializedName("timezone")
    @Expose
    private Timezone timezone;

    public Bitrate getBitrate() {
        return bitrate;
    }

    public void setBitrate(Bitrate bitrate) {
        this.bitrate = bitrate;
    }

    public HttpCaching getHttpCaching() {
        return httpCaching;
    }

    public void setHttpCaching(HttpCaching httpCaching) {
        this.httpCaching = httpCaching;
    }

    public StreamServer getStreamServer() {
        return streamServer;
    }

    public void setStreamServer(StreamServer streamServer) {
        this.streamServer = streamServer;
    }

    public Timeshift getTimeshift() {
        return timeshift;
    }

    public void setTimeshift(Timeshift timeshift) {
        this.timeshift = timeshift;
    }

    public Timezone getTimezone() {
        return timezone;
    }

    public void setTimezone(Timezone timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "bitrate=" + bitrate +
                ", httpCaching=" + httpCaching +
                ", streamServer=" + streamServer +
                ", timeshift=" + timeshift +
                ", timezone=" + timezone +
                '}';
    }
}
