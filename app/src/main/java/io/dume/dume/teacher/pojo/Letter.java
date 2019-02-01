package io.dume.dume.teacher.pojo;

import java.util.Date;

public class Letter {

    String uid;
    String body;
    Date timestamp;

    Letter() {

    }

    public Letter(String uid, String body, Date timestamp) {
        this.uid = uid;
        this.body = body;
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
