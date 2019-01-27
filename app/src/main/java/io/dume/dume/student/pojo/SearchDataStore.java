package io.dume.dume.student.pojo;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;
import com.touchboarder.weekdaysbuttons.WeekdaysDataItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dume.dume.util.DumeUtils;

import static io.dume.dume.student.grabingPackage.GrabingPackageActivity.PlaceholderFragment.getSelectedDaysFromWeekdaysData;

public class SearchDataStore implements Serializable {
    public static String TEACHER = "teacher";
    public static String STUDENT = "student";
    public static String BOOTCAMP = "bootcamp";
    public static String DUME_GANG = "Dume Gang";
    public static String REGULAR_DUME = "Regular Dume";
    public static String INSTANT_DUME = "Instant Dume";
    public static int SHORTRADIUS = 300;
    public static int LONGRADIUS = 600;

    private String userName;
    private String userNumber;
    private String userMail;
    private String avatarString = null;
    private String gender = "Male";
    private Map<String, Object> documentSnapshot;

    private String userUid;
    private LatLng anchorPoint;
    private String packageName;
    private Map<String, Object> jizz;
    private List<String> queryList;
    private List<String> queryListName;
    private Map<String, Object> forWhom;
    private Map<String, Object> preferredDays;
    private Map<String, Object> startTime;
    private Map<String, Object> startDate;
    private Map<String, Object> mainMap;


    private static SearchDataStore instance = null;

    public static SearchDataStore getInstance() {
        if (instance == null) {
            instance = new SearchDataStore();
        }
        return instance;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getQueryString() {

        return DumeUtils.generateQueryString(packageName, queryList, queryListName);
    }

    public SearchDataStore() {
    }

    public Map<String, Object> getDocumentSnapshot() {
        return documentSnapshot;
    }

    public void setDocumentSnapshot(Map<String, Object> documentSnapshot) {
        this.documentSnapshot = documentSnapshot;
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

    public List<String> getQueryList() {
        return queryList;
    }

    public void setQueryList(List<String> queryList) {
        this.queryList = queryList;
    }

    public List<String> getQueryListName() {
        return queryListName;
    }

    public void setQueryListName(List<String> queryListName) {
        this.queryListName = queryListName;
    }

    public Map<String, Object> getMainMap() {
        return mainMap;
    }

    public void setMainMap(Map<String, Object> mainMap) {
        this.mainMap = mainMap;
    }

    public Map<String, Object> genSetRetForWhom(String name, String number, String uid, Bitmap photo, Boolean self) {
        Map<String, Object> generatedforWhom = new HashMap<>();
        generatedforWhom.put("name", name);
        generatedforWhom.put("phone_number", number);
        generatedforWhom.put("request_uid", uid);
        generatedforWhom.put("photo", photo);
        generatedforWhom.put("is_self", self);
        generatedforWhom.put("email", getUserMail());
        setForWhom(generatedforWhom);
        return generatedforWhom;
    }

    public Map<String, Object> genSetRetJizz(List<String> queryList, List<String> queryListName) {
        Map<String, Object> queryMap = new HashMap<>();
        setQueryList(queryList);
        setQueryListName(queryListName);
        for (int i = 0; i < queryList.size(); i++) {
            queryMap.put(queryListName.get(i), queryList.get(i));
        }
        setJizz(queryMap);
        return queryMap;
    }

    public Map<String, Object> genSetRetPreferredDays(String selectedDays, List<Integer> selectedDaysInt) {
        Map<String, Object> generatedPreferredDays = new HashMap<>();
        generatedPreferredDays.put("days_per_week", selectedDaysInt.size());
        generatedPreferredDays.put("selected_days", selectedDays);
        generatedPreferredDays.put("selectedDaysInt", selectedDaysInt);
        setPreferredDays(generatedPreferredDays);
        return generatedPreferredDays;
    }

    public Map<String, Object> genSetRetStartDate(Integer year, Integer month, Integer dayOfMonth, String currentDateStr) {
        Map<String, Object> generatedStartDate = new HashMap<>();
        generatedStartDate.put("date_string", currentDateStr);
        generatedStartDate.put("year", year);
        generatedStartDate.put("month", month);
        generatedStartDate.put("day_of_month", dayOfMonth);
        setStartDate(generatedStartDate);
        return generatedStartDate;
    }

    public Map<String, Object> genSetRetStartTime(Integer hourOfDay, Integer minute, String myTime) {
        Map<String, Object> generatedStartTime = new HashMap<>();
        generatedStartTime.put("hour_of_day", hourOfDay);
        generatedStartTime.put("minute", minute);
        generatedStartTime.put("time_string", myTime);
        setStartTime(generatedStartTime);
        return generatedStartTime;
    }

    //generate main map
    public Map<String, Object> genRetMainMap() {
        Map<String, Object> generatedMainMap = new HashMap<>();
        GeoPoint myAnchorPoint = new GeoPoint(anchorPoint.latitude, anchorPoint.longitude);
        generatedMainMap.put("user_uid", userUid);
        generatedMainMap.put("anchor_point", myAnchorPoint);
        generatedMainMap.put("package_name", packageName);
        generatedMainMap.put("jizz", jizz);
        generatedMainMap.put("query_list", queryList);
        generatedMainMap.put("query_list_name", queryListName);
        generatedMainMap.put("for_whom", forWhom);
        generatedMainMap.put("preferred_days", preferredDays);
        generatedMainMap.put("start_date", startDate);
        generatedMainMap.put("start_time", startTime);

        setMainMap(generatedMainMap);
        return generatedMainMap;
    }


}
