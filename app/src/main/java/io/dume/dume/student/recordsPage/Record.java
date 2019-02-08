package io.dume.dume.student.recordsPage;

public class Record {
    public static int DELIVERED = 2, NOT_DELIVERED = 1, DELIVERED_SEEN = 3;

    String mentorName;
    String studentName;
    String salaryInDemand;
    String subjectExchange;
    String date;
    String mentorDpUrl;
    String studentDpUrl;
    float studentRating;
    float mentorRating;
    String status;
    int deliveryStatus;

    public Record(String mentorName, String studentName, String salaryInDemand, String subjectExchange, String date, String mentorDpUrl, String studentDpUrl, float studentRating, float mentorRating, String status, int deliveryStatus) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
}
