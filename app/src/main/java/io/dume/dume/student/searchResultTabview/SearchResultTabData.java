package io.dume.dume.student.searchResultTabview;

import android.support.annotation.Keep;

import java.io.Serializable;

@Keep
public class SearchResultTabData implements Serializable {

    String mentorDPUrl;
    String mentorName;
    String salary;
    String rating;
    String expertise;
    String a_ratio;
    int mentorFilterImage;
    String gender;
    String mentorUid;
    String documentUid;
    Integer identify;
    Integer mSalary;
    Float mRating;
    Integer mAcceptRatio;
    Integer mExpirtise;

    public SearchResultTabData(String mentorDPUrl, String mentorName, String salary, String rating, String expertise, String a_ratio, int mentorFilterImage, String gender, String mentorUid, String documentUid, Integer identify) {
        this.mentorDPUrl = mentorDPUrl;
        this.mentorName = mentorName;
        this.salary = salary;
        this.rating = rating;
        this.expertise = expertise;
        this.a_ratio = a_ratio;
        this.mentorFilterImage = mentorFilterImage;
        this.gender = gender;
        this.mentorUid = mentorUid;
        this.documentUid = documentUid;
        this.identify = identify;
    }

    public SearchResultTabData() {
    }

    public String getMentorDPUrl() {
        return mentorDPUrl;
    }

    public void setMentorDPUrl(String mentorDPUrl) {
        this.mentorDPUrl = mentorDPUrl;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public int getMentorFilterImage() {
        return mentorFilterImage;
    }

    public void setMentorFilterImage(int mentorFilterImage) {
        this.mentorFilterImage = mentorFilterImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getIdentify() {
        return identify;
    }

    public void setIdentify(Integer identify) {
        this.identify = identify;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getA_ratio() {
        return a_ratio;
    }

    public void setA_ratio(String a_ratio) {
        this.a_ratio = a_ratio;
    }

    public String getMentorUid() {
        return mentorUid;
    }

    public void setMentorUid(String mentorUid) {
        this.mentorUid = mentorUid;
    }

    public String getDocumentUid() {
        return documentUid;
    }

    public void setDocumentUid(String documentUid) {
        this.documentUid = documentUid;
    }


}
