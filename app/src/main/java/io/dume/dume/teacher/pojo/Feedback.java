package io.dume.dume.teacher.pojo;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class Feedback implements Serializable {
    private String value;
    private String valueTitle;

    public Feedback(String value, String valueTitle) {
        this.value = value;
        this.valueTitle = valueTitle;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueTitle() {
        return valueTitle;
    }

    public void setValueTitle(String valueTitle) {
        this.valueTitle = valueTitle;
    }
}
