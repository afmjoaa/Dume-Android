package io.dume.dume.teacher.pojo;

import android.support.annotation.Keep;
import java.io.Serializable;

@Keep
public class Education implements Serializable {
    private int from, to;
    private String title, degree, description;

    public Education(int from, int to, String title, String degree, String description) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.degree = degree;
        this.description = description;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFrom() {

        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }
}
