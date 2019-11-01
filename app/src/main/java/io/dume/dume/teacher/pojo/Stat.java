package io.dume.dume.teacher.pojo;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Date;
@Keep
public class Stat implements Serializable {
    private String request_i;
    private String request_r;
    private Date creation;
    private String uid;
    private int identify;

    public Stat() {
    }

    public Stat(String request_i, String request_r, Date creation, String uid) {
        this.request_i = request_i;
        this.request_r = request_r;
        this.creation = creation;
        this.uid = uid;
    }

    public String getRequest_i() {
        return request_i;
    }

    public void setRequest_i(String request_i) {
        this.request_i = request_i;
    }

    public String getRequest_r() {
        return request_r;
    }

    public void setRequest_r(String request_r) {
        this.request_r = request_r;
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIdentify() {
        return identify;
    }

    public void setIdentify(int identify) {
        this.identify = identify;
    }
}
