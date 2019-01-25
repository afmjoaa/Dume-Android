package io.dume.dume.teacher.model;

public class KeyMap {
    String title;
    Object value;

    public KeyMap(String title, Object value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
