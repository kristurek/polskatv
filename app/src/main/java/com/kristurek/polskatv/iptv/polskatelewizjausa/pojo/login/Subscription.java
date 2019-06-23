
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscription {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("contents_type")
    @Expose
    private String contentsType;
    @SerializedName("option")
    @Expose
    private String option;
    @SerializedName("continuous")
    @Expose
    private String continuous;
    @SerializedName("begin_date")
    @Expose
    private String beginDate;
    @SerializedName("type_id")
    @Expose
    private String typeId;
    @SerializedName("type_ru")
    @Expose
    private String typeRu;
    @SerializedName("type_en")
    @Expose
    private String typeEn;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("rest_of_days")
    @Expose
    private String restOfDays;

    public String getTitle() {
        return title;
    }

    public String getContentsType() {
        return contentsType;
    }

    public String getOption() {
        return option;
    }

    public String getContinuous() {
        return continuous;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getTypeRu() {
        return typeRu;
    }

    public String getTypeEn() {
        return typeEn;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getRestOfDays() {
        return restOfDays;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Subscription{");
        sb.append("title='").append(title).append('\'');
        sb.append(", contentsType='").append(contentsType).append('\'');
        sb.append(", option='").append(option).append('\'');
        sb.append(", continuous='").append(continuous).append('\'');
        sb.append(", beginDate='").append(beginDate).append('\'');
        sb.append(", typeId='").append(typeId).append('\'');
        sb.append(", typeRu='").append(typeRu).append('\'');
        sb.append(", typeEn='").append(typeEn).append('\'');
        sb.append(", endDate='").append(endDate).append('\'');
        sb.append(", restOfDays='").append(restOfDays).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
