package io.dume.dume.teacher.pojo;

import java.util.Date;

public class Stat {
    private int impression;
    private int view;
    private int click;
    private Date date;

    public int getImpression() {
        return impression;
    }

    public void setImpression(int impression) {
        this.impression = impression;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getClick() {
        return click;
    }

    public void setClick(int click) {
        this.click = click;
    }

    public Stat(int impression, int view, int click, Date date) {
        this.impression = impression;
        this.view = view;
        this.click = click;
        this.date = date;
    }
}
