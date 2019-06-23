
package com.kristurek.polskatv.iptv.polskatelewizjausa.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Account {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("balance")
    @Expose
    private String balance;
    @SerializedName("acc_level")
    @Expose
    private String accLevel;
    @SerializedName("node_id")
    @Expose
    private String nodeId;
    @SerializedName("subscriptions")
    @Expose
    private List<Subscription> subscriptions = null;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBalance() {
        return balance;
    }

    public String getAccLevel() {
        return accLevel;
    }

    public String getNodeId() {
        return nodeId;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("id='").append(id).append('\'');
        sb.append(", title='").append(title).append('\'');
        sb.append(", balance='").append(balance).append('\'');
        sb.append(", accLevel='").append(accLevel).append('\'');
        sb.append(", nodeId='").append(nodeId).append('\'');
        sb.append(", subscriptions=").append(subscriptions);
        sb.append('}');
        return sb.toString();
    }
}
