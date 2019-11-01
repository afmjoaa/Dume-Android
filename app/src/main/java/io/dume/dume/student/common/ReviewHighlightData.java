package io.dume.dume.student.common;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.Date;

@Keep
public class ReviewHighlightData implements Serializable {
    public String name;
    public String body;
    public Date time;
    public String reviewer_rating;
    public int likes;
    public int dislikes;
    public String r_avatar;
    public String doc_id;

    public ReviewHighlightData(String name, String body, Date time, String reviewer_rating, int likes, int dislikes, String r_avatar) {
        this.name = name;
        this.body = body;
        this.time = time;
        this.reviewer_rating = reviewer_rating;
        this.likes = likes;
        this.dislikes = dislikes;
        this.r_avatar = r_avatar;
    }

    public ReviewHighlightData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getReviewer_rating() {
        return reviewer_rating;
    }

    public void setReviewer_rating(String reviewer_rating) {
        this.reviewer_rating = reviewer_rating;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public String getR_avatar() {
        return r_avatar;
    }

    public void setR_avatar(String r_avatar) {
        this.r_avatar = r_avatar;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }
}
