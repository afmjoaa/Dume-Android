package io.dume.dume.student.homePage.adapter;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.List;

import io.dume.dume.student.recordsPage.Record;

@Keep
public class HomePageRatingData implements Serializable {
    List<String> ratingNameList;
    String name;
    String avatar;
    Record record;

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public HomePageRatingData() {

    }

    public HomePageRatingData(List<String> ratingNameList,String name, String avatar) {
        this.ratingNameList = ratingNameList;
        this.name = name;
        this.avatar = avatar;
    }

    public List<String> getRatingNameList() {
        return ratingNameList;
    }

    public void setRatingNameList(List<String> ratingNameList) {
        this.ratingNameList = ratingNameList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
