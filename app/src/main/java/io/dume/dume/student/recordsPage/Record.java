package io.dume.dume.student.recordsPage;

import android.support.annotation.Keep;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

@Keep
public class Record {
    public static int DELIVERED = 2, NOT_DELIVERED = 1, DELIVERED_SEEN = 3;
    public static final String DIALOG = "dialog", BOTTOM_SHEET = "bottom_sheet", DONE = "done";


    String mentorName;
    String studentName;
    String salaryInDemand;
    String subjectExchange;
    Date date;
    Date modiDate;
    String mentorDpUrl;
    String studentDpUrl;
    float studentRating;

    public DocumentSnapshot getRecordSnap() {
        return recordSnap;
    }

    public void setRecordSnap(DocumentSnapshot recordSnap) {
        this.recordSnap = recordSnap;
    }

    float mentorRating;
    String status;
    int deliveryStatus;
    String sGender, mGender;
    String t_rate_status;
    String s_rate_status;

    DocumentSnapshot recordSnap;

    public Record(String mentorName, String studentName, String salaryInDemand, String subjectExchange, Date date,
                  String mentorDpUrl, String studentDpUrl, float studentRating, float mentorRating,
                  String status, int deliveryStatus, String studentGender, String mentorGender) {
        this.mentorName = mentorName;
        this.studentName = studentName;
        this.salaryInDemand = salaryInDemand;
        this.subjectExchange = subjectExchange;
        this.date = date;
        this.mentorDpUrl = mentorDpUrl;
        this.studentDpUrl = studentDpUrl;
        this.studentRating = studentRating;
        this.mentorRating = mentorRating;
        this.status = status;
        this.deliveryStatus = deliveryStatus;
        this.sGender = studentGender;
        this.mGender = mentorGender;
    }

    public Date getModiDate() {
        return modiDate;
    }

    public void setModiDate(Date modiDate) {
        this.modiDate = modiDate;
    }

    public String getT_rate_status() {
        return t_rate_status;
    }

    public void setT_rate_status(String t_rate_status) {
        this.t_rate_status = t_rate_status;
    }

    public String getS_rate_status() {
        return s_rate_status;
    }

    public void setS_rate_status(String s_rate_status) {
        this.s_rate_status = s_rate_status;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getSalaryInDemand() {
        return salaryInDemand;
    }

    public void setSalaryInDemand(String salaryInDemand) {
        this.salaryInDemand = salaryInDemand;
    }

    public String getSubjectExchange() {
        return subjectExchange;
    }

    public void setSubjectExchange(String subjectExchange) {
        this.subjectExchange = subjectExchange;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMentorDpUrl() {
        return mentorDpUrl;
    }

    public void setMentorDpUrl(String mentorDpUrl) {
        this.mentorDpUrl = mentorDpUrl;
    }

    public String getStudentDpUrl() {
        return studentDpUrl;
    }

    public void setStudentDpUrl(String studentDpUrl) {
        this.studentDpUrl = studentDpUrl;
    }

    public float getStudentRating() {
        return studentRating;
    }

    public void setStudentRating(float studentRating) {
        this.studentRating = studentRating;
    }

    public float getMentorRating() {
        return mentorRating;
    }

    public void setMentorRating(float mentorRating) {
        this.mentorRating = mentorRating;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(int deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getsGender() {
        return sGender;
    }

    public void setsGender(String sGender) {
        this.sGender = sGender;
    }

    public String getmGender() {
        return mGender;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }
}
