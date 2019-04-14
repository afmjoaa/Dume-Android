package io.dume.dume.common.contactActivity;


import android.support.annotation.Keep;

import java.util.Map;
import java.util.Objects;

@Keep
public class ContactData {
    //1 == Pending
//2 == Accepted
//3 == Current

    public String getRecord_id() {
        return record_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    String contactUserDP;
    String contactUserName;
    String status;
    String userId;
    String record_id;
    String phone;
    int flagStatus;
    Map<String, Object> record;

    public Map<String, Object> getRecord() {
        return record;
    }

    public void setRecord(Map<String, Object> record) {
        this.record = record;
    }


    ContactData(String record_id, String contactUserDP, String contactUserName, String status, String userId, Map<String, Object> record, String phone) {

        this.record_id = record_id;
        this.record = record;

        this.userId = userId;
        this.contactUserDP = contactUserDP;
        this.contactUserName = contactUserName;
        this.status = status;
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContactUserDP() {
        return contactUserDP;
    }

    public void setContactUserDP(String contactUserDP) {
        this.contactUserDP = contactUserDP;
    }

    public String getContactUserName() {
        return contactUserName;
    }

    public void setContactUserName(String contactUserName) {
        this.contactUserName = contactUserName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFlagStatus() {
        return flagStatus;
    }

    public void setFlagStatus(int flagStatus) {
        this.flagStatus = flagStatus;
    }

    @Override
    public boolean equals(Object object) {
        boolean sameSame = false;
        if (object != null && object instanceof ContactData) {
            sameSame = this.userId == ((ContactData) object).userId;
        }

        return sameSame;
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return getUserId();
    }
}
