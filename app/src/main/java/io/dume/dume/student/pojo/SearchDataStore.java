package io.dume.dume.student.pojo;

import androidx.annotation.Keep;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.dume.dume.util.DumeUtils;

@Keep
public class SearchDataStore implements Serializable {
    public static String TEACHER = "teacher";
    public static String STUDENT = "student";
    public static String BOOTCAMP = "bootcamp";
    public static String DUME_GANG = "Dume Gang";
    public static String REGULAR_DUME = "Regular Dume";
    public static String INSTANT_DUME = "Instant Dume";

    public static String DEFAULTMALEAVATER = "https://firebasestorage.googleapis.com/v0/b/dume-2d063.appspot.com/o/avatar.png?alt=media&token=801c75b7-59fe-4a13-9191-186ef50de707";
    public static String DEFAULTFEMALEAVATER = "https://firebasestorage.googleapis.com/v0/b/dume-2d063.appspot.com/o/tavatar_female.png?alt=media&token=1f96bade-ab4d-4e7f-9b43-d27104cfc35f";
    public static String DEFAULTUSERAVATER = "https://firebasestorage.googleapis.com/v0/b/dume-2d063.appspot.com/o/user.png?alt=media&token=36cf9d33-7b4a-413a-9434-864fcd9e1559";
    public static String BOYSTUDENT = "https://firebasestorage.googleapis.com/v0/b/dume-2d063.appspot.com/o/boy.png?alt=media&token=5d3c72fe-d546-4089-b2bb-bf433c457fc3";
    public static String GIRLSTUDENT = "https://firebasestorage.googleapis.com/v0/b/dume-2d063.appspot.com/o/girl.png?alt=media&token=bfc9872c-5833-459d-96f8-4395f6aa776c";

    public static int SHORTRADIUS = 2000;
    public static int MEDIUMSHORTRADIUS = 8000;
    public static int MEDIUMLONGRADIUS = 16000;
    public static int LONGRADIUS = 32000;

    public static String STATUSPENDING = "Pending";
    public static String STATUSACCEPTED = "Accepted";
    public static String STATUSCURRENT = "Current";
    public static String STATUSCOMPLETED = "Completed";
    public static String STATUSREJECTED = "Rejected";
    private Boolean recordStatusChanged = false;
    private Integer fromPACCR = -1;

    private List<DocumentSnapshot> resultList;
    private Integer levelNum = 1;

    private Boolean profileChanged = false;
    private Boolean isFirstTime = true;
    private Boolean isFirstTimeEnam = true;
    private String selectedMentor = null;

    public Integer getFromPACCR() {
        return fromPACCR;
    }

    public void setFromPACCR(Integer fromPACCR) {
        this.fromPACCR = fromPACCR;
    }

    public Boolean getRecordStatusChanged() {
        return recordStatusChanged;
    }

    public void setRecordStatusChanged(Boolean recordStatusChanged) {
        this.recordStatusChanged = recordStatusChanged;
    }

    public String getSelectedMentor() {
        return selectedMentor;
    }

    public void setSelectedMentor(String selectedMentor) {
        this.selectedMentor = selectedMentor;
    }

    public Boolean getFirstTimeEnam() {
        return isFirstTimeEnam;
    }

    public void setFirstTimeEnam(Boolean firstTimeEnam) {
        isFirstTimeEnam = firstTimeEnam;
    }

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

    public List<DocumentSnapshot> getResultList() {
        return resultList;
    }

    public void setResultList(List<DocumentSnapshot> resultList) {

        this.resultList = resultList;
    }

    private static SearchDataStore instance = null;

    public static SearchDataStore getInstance() {
        if (instance == null) {
            instance = new SearchDataStore();
        }
        return instance;
    }

    public Integer getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
    }

    public Boolean getFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(Boolean firstTime) {
        isFirstTime = firstTime;
    }

    public Boolean getProfileChanged() {
        return profileChanged;
    }

    public void setProfileChanged(Boolean profileChanged) {
        this.profileChanged = profileChanged;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getQueryString() {

        return DumeUtils.generateQueryString(packageName, getQueryList(), getQueryListName());
    }

    public Map<String, Object> getQueryMap() {

        return DumeUtils.getQueryMap(packageName, getQueryList(), getQueryListName());
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

    public Map<String, Object> genSetRetForWhom(String name, String number, String uid, String photo, Boolean self) {
        Map<String, Object> generatedforWhom = new HashMap<>();
        generatedforWhom.put("stu_name", name);
        generatedforWhom.put("stu_phone_number", number);
        generatedforWhom.put("stu_photo", photo);
        generatedforWhom.put("is_self", self);

        String o = (String) documentSnapshot.get("last_name");
        String o1 = (String) documentSnapshot.get("first_name");
        String request_user_name = o1 + " " + o;
        generatedforWhom.put("request_email", getUserMail());
        generatedforWhom.put("request_uid", uid);
        generatedforWhom.put("request_user_name", request_user_name);
        generatedforWhom.put("request_phone_number", documentSnapshot.get("phone_number"));
        generatedforWhom.put("request_avatar", documentSnapshot.get("avatar"));
        generatedforWhom.put("request_cs", documentSnapshot.get("current_status"));
        generatedforWhom.put("request_gender", documentSnapshot.get("gender"));
        generatedforWhom.put("request_pr", documentSnapshot.get("previous_result"));
        generatedforWhom.put("request_sr", documentSnapshot.get("self_rating"));
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