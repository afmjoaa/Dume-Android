package io.dume.dume.teacher.homepage;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.dume.dume.student.pojo.SearchDataStore;
import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.teacher.pojo.Stat;

public class TeacherDataStore implements Serializable {
    private Map<String, Object> selfRating = null;
    private Map<String, Object> documentSnapshot;
    private List<Stat> stat;
    private List<Stat> todayStatList;
    private static TeacherDataStore teacherDataStore = null;
    private String packName;
    private ArrayList<Skill> skillArrayList = null;

    private String tUserName;
    private String tUserNumber;
    private String tUserMail;
    private String tAvatarString = null;
    private String tUserUid;
    private String packageName;

    public String getPackageName() {
        return packageName == null ? SearchDataStore.REGULAR_DUME : packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public static TeacherDataStore getInstance() {
        if (teacherDataStore == null) {
            teacherDataStore = new TeacherDataStore();
        }
        return teacherDataStore;
    }

    public List<Stat> getTodayStatList() {
        return todayStatList;
    }

    public void setTodayStatList(List<Stat> todayStatList) {
        this.todayStatList = todayStatList;
    }

    public List<Stat> getStat() {
        return stat;
    }

    public void setStat(List<Stat> stat) {
        this.stat = stat;
    }

    public ArrayList<Skill> getSkillArrayList() {
        return skillArrayList;
    }

    public void setSkillArrayList(ArrayList<Skill> skillArrayList) {
        this.skillArrayList = skillArrayList;
    }

    public Map<String, Object> getSelfRating() {
        return selfRating;
    }

    public void setSelfRating(Map<String, Object> selfRating) {
        this.selfRating = selfRating;
    }

    public Map<String, Object> getDocumentSnapshot() {
        return documentSnapshot;
    }

    public void setDocumentSnapshot(Map<String, Object> documentSnapshot) {
        this.documentSnapshot = documentSnapshot;
    }

    public String gettUserName() {
        return tUserName;
    }

    public void settUserName(String tUserName) {
        this.tUserName = tUserName;
    }

    public String gettUserNumber() {
        return tUserNumber;
    }

    public void settUserNumber(String tUserNumber) {
        this.tUserNumber = tUserNumber;
    }

    public String gettUserMail() {
        return tUserMail;
    }

    public void settUserMail(String tUserMail) {
        this.tUserMail = tUserMail;
    }

    public String gettAvatarString() {
        return tAvatarString;
    }

    public void settAvatarString(String tAvatarString) {
        this.tAvatarString = tAvatarString;
    }

    public String gettUserUid() {
        return tUserUid;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public void settUserUid(String tUserUid) {
        this.tUserUid = tUserUid;
    }
}
