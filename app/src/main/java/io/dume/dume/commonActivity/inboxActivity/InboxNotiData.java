package io.dume.dume.commonActivity.inboxActivity;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Date;

@Keep
public class InboxNotiData implements Serializable {
    public static String RECORD = "record",
            PROMO = "promo",
            CANCEL = "cancel",
            GLOBAL = "global";

    String title;
    String body;
    String avatar;
    String name;
    String mail;
    boolean seen;
    String type;
    String doc_id;
    String token;
    String uid;
    Date date;

    public Date getTimestapm() {
        return date;
    }

    public void setTimestapm(Date timestapm) {
        this.date = timestapm;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public InboxNotiData() {

    }

    public InboxNotiData(String title, String body, String avatar, String name, String mail, boolean seen, String type, String doc_id, String token, String uid) {
        this.title = title;
        this.body = body;
        this.avatar = avatar;
        this.name = name;
        this.mail = mail;
        this.seen = seen;
        this.type = type;
        this.doc_id = doc_id;
        this.token = token;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
