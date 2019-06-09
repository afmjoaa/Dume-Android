package io.dume.dume.teacher.pojo;

import android.support.annotation.Keep;

import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
@Keep
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
    @PropertyName("map")
    HashMap<String, Object> likes;
    @PropertyName("map")
    HashMap<String, Object> dislikes;

    String package_name;
    String id;
    String commonQueryString;

    List<String> query_list_name;
    List<String> query_list;

    public String getCommonQueryString() {
        return commonQueryString;
    }

    public void setCommonQueryString(String commonQueryString) {
        this.commonQueryString = commonQueryString;
    }

    public List<String> getQuery_list_name() {
        return query_list_name;
    }

    public void setQuery_list_name(List<String> query_list_name) {
        this.query_list_name = query_list_name;
    }

    public List<String> getQuery_list() {
        return query_list;
    }

    public void setQuery_list(List<String> query_list) {
        this.query_list = query_list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public HashMap<String, Object> getFeedback() {
        return feedback;
    }

    public void setFeedback(HashMap<String, Object> feedback) {
        this.feedback = feedback;
    }

    public Skill() {

    }

    public Skill(boolean status, String gender, float salary, Date creationDate, HashMap<String, Object> map, String queryString, GeoPoint location, int totalRating, int enrolledStudent, String mentor_uid, float rating, HashMap<String, Object> likes, HashMap<String, Object> dislikes, String package_name) {
        this.package_name = package_name;
        this.likes = likes;
        this.dislikes = dislikes;
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

    public HashMap<String, Object> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, Object> likes) {
        this.likes = likes;
    }

    public HashMap<String, Object> getDislikes() {
        return dislikes;
    }

    public void setDislikes(HashMap<String, Object> dislikes) {
        this.dislikes = dislikes;
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
