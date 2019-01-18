package io.dume.dume.student.pojo;

import com.google.android.gms.maps.model.LatLng;
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchDataStore implements Serializable {
    private LatLng anchorPoint;
    private Map<String, Object> jizz;
    private Map<String, Object> forWhom;
    private String packageName;
    private Map<String, Object> packageJizz;

    public SearchDataStore(LatLng anchorPoint, Map<String, Object> jizz, Map<String, Object> forWhom, String packageName, Map<String, Object> packageJizz) {
        this.anchorPoint = anchorPoint;
        this.jizz = jizz;
        this.forWhom = forWhom;
        this.packageName = packageName;
        this.packageJizz = packageJizz;
    }

    public SearchDataStore() {
    }

    public LatLng getAnchorPoint() {
        return anchorPoint;
    }

    public void setAnchorPoint(LatLng anchorPoint) {
        this.anchorPoint = anchorPoint;
    }

    public Map<String, Object> getJizz() {
        return jizz;
    }

    public void setJizz(Map<String, Object> jizz) {
        this.jizz = jizz;
    }

    public Map<String, Object> getForWhom() {
        return forWhom;
    }

    public void setForWhom(Map<String, Object> forWhom) {
        this.forWhom = forWhom;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Map<String, Object> getPackageJizz() {
        return packageJizz;
    }

    public void setPackageJizz(Map<String, Object> packageJizz) {
        this.packageJizz = packageJizz;
    }

    public Map<String, Object> genSetRetPackageJizz(String daysPerWeek, ArrayList<WeekdaysDataItem> preferredDays, String startDate, String startTime){
        //TODO
        return null;
    }

    public Map<String, Object> genSetRetForWhom(String name, String number, String avatarString, String uid, boolean self){
        //TODO
        return  null;
    }

    public Map<String, Object> getSetRetJizz(List<String> queryList, List<String> queryListName){
        //TODO
        return null;
    }
}
