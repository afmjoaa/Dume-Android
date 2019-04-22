package io.dume.dume.common.inboxActivity;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.net.URL;

@Keep
public class InboxCallData implements Serializable {
    String userName;
    String avatar;
    String phoneNumber;
    String uid;

    public InboxCallData(String userName, String avatar, String phoneNumber, String uid) {
        this.userName = userName;
        this.avatar = avatar;
        this.phoneNumber = phoneNumber;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
