package io.dume.dume.teacher.pojo;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Skill implements Serializable {
    @PropertyName("status")
    boolean status = true;
    @PropertyName("gender")
    String gender;
    @PropertyName("salary")
    float salary;
    @PropertyName("creation_date")
    Date creation;
    @PropertyName("map")
    HashMap<String, Object> jizz;
    @PropertyName("query_string")
    String query_string;
    @PropertyName("location")
    GeoPoint location;
    @PropertyName("totalRating")
    int totalRating;
    @PropertyName("enrolled")
    int enrolled;
    @PropertyName("mentor_uid")
    String mentor_uid;
    @PropertyName("rating")
    float rating;
    HashMap<String, Object> feedback;

    public HashMap<String, Object> getFeedback() {
        return feedback;
    }

    public void setFeedback(HashMap<String, Object> feedback) {
        this.feedback = feedback;
    }

    public Skill() {

    }

    public Skill(boolean status, String gender, float salary, Date creationDate, HashMap<String, Object> map, String queryString, GeoPoint location, int totalRating, int enrolledStudent, String mentor_uid, float rating) {
        this.status = status;
        this.gender = gender;
        this.salary = salary;
        this.creation = creationDate;
        this.jizz = map;
        this.query_string = queryString;
        this.location = location;
        this.totalRating = totalRating;
        this.enrolled = enrolledStudent;
        this.mentor_uid = mentor_uid;
        this.rating = rating;
    }

    public int getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(int enrolled) {
        this.enrolled = enrolled;
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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public HashMap<String, Object> getJizz() {
        return jizz;
    }

    public void setJizz(HashMap<String, Object> jizz) {
        this.jizz = jizz;
    }

    public String getQuery_string() {
        return query_string;
    }

    public void setQuery_string(String query_string) {
        this.query_string = query_string;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
