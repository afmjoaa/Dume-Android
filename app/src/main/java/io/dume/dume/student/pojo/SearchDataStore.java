package io.dume.dume.student.pojo;

import com.google.android.gms.maps.model.LatLng;
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.dume.dume.student.grabingPackage.GrabingPackageActivity.PlaceholderFragment.getSelectedDaysFromWeekdaysData;

public class SearchDataStore implements Serializable {
    public static String TEACHER = "teacher";
    public static String STUDENT = "student";
    public static String BOOTCAMP = "bootcamp";
    public  static int SHORTRADIUS = 300;
    public  static int LONGRADIUS = 600;

    private String userName;
    private String userNumber;
    private String userMail;
    private String avatarString;
    private String userUid;

    private LatLng anchorPoint;
    private Map<String, Object> jizz;
    private Map<String, Object> forWhom;
    private String packageName;
    private String daysPerWeek;
    private Map<String, Object> preferredDays;
    private Map<String, Object> startTime;
    private Map<String, Object> startDate;

    public String getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(String daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public Map<String, Object> getPreferredDays() {
        return preferredDays;
    }

    public void setPreferredDays(Map<String, Object> preferredDays) {
        this.preferredDays = preferredDays;
    }

    public Map<String, Object> getStartTime() {
        return startTime;
    }

    public void setStartTime(Map<String, Object> startTime) {
        this.startTime = startTime;
    }

    public Map<String, Object> getStartDate() {
        return startDate;
    }

    public void setStartDate(Map<String, Object> startDate) {
        this.startDate = startDate;
    }

    private static SearchDataStore instance = null;

    public static SearchDataStore getInstance() {
        if (instance == null) {
            instance = new SearchDataStore();
        }
        return instance;
    }


    public SearchDataStore() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getAvatarString() {
        return avatarString;
    }

    public void setAvatarString(String avatarString) {
        this.avatarString = avatarString;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public static void setInstance(SearchDataStore instance) {
        SearchDataStore.instance = instance;
    }

    //main part start
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


    public Map<String, Object> genSetRetForWhom(String name, String number, String uid, boolean self) {
        Map<String, Object> generatedforWhom = new HashMap<>();
        generatedforWhom.put("name", name);
        generatedforWhom.put("phone_number", number);
        generatedforWhom.put("request_uid", uid);
        generatedforWhom.put("is_self", self);
        setForWhom(generatedforWhom);
        return generatedforWhom;
    }

    public Map<String, Object> genSetRetJizz(List<String> queryList, List<String> queryListName) {
        Map<String, Object> queryMap = new HashMap<>();
        for (int i = 0; i < queryList.size(); i++) {
            queryMap.put(queryListName.get(i), queryList.get(i));
        }
        setJizz(queryMap);
        return queryMap;
    }

    public Map<String, Object> genSetRetStartTime() {
        //TODO
        Map<String, Object> generatedStartTime = new HashMap<>();
        return generatedStartTime;
    }

    public Map<String, Object> genSetRetStartDate() {
        //TODO
        Map<String, Object> generatedStartDate = new HashMap<>();
        return generatedStartDate;
    }

    public Map<String, Object> genSetRetPreferredDays() {
        //TODO
        Map<String, Object> generatedStartPDays = new HashMap<>();
        return generatedStartPDays;
    }

    //generate main map
    public Map<String, Object> genRetMainMap() {
        //TODO
        Map<String, Object> generatedMainMap = new HashMap<>();
        return generatedMainMap;
    }
}
