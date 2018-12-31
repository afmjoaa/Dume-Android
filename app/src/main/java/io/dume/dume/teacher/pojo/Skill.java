package io.dume.dume.teacher.pojo;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.HashMap;

public class Skill {
    private boolean status = true;
    private String gender;
    private float salary;
    private Date creationDate;
    private HashMap<String, Object> map;
    private String queryString;
    private LatLng location;
    private int totalRating;
    private int enrolledStudent;
    private String mentor_uid;
    private float ratingValue;

    public Skill(boolean status, String gender, float salary, Date creationDate, HashMap<String, Object> map, String queryString, LatLng location, int totalRating, int enrolledStudent, String mentor_uid, float ratingValue) {
        this.status = status;
        this.gender = gender;
        this.salary = salary;
        this.creationDate = creationDate;
        this.map = map;
        this.queryString = queryString;
        this.location = location;
        this.totalRating = totalRating;
        this.enrolledStudent = enrolledStudent;
        this.mentor_uid = mentor_uid;
        this.ratingValue = ratingValue;
    }

    public int getEnrolledStudent() {
        return enrolledStudent;
    }

    public void setEnrolledStudent(int enrolledStudent) {
        this.enrolledStudent = enrolledStudent;
    }

    public String getMentor_uid() {
        return mentor_uid;
    }

    public void setMentor_uid(String mentor_uid) {
        this.mentor_uid = mentor_uid;
    }

    public int getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(int totalRating) {
        this.totalRating = totalRating;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public HashMap<String, Object> getMap() {
        return map;
    }

    public void setMap(HashMap<String, Object> map) {
        this.map = map;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
