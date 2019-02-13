package io.dume.dume;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import io.dume.dume.common.chatActivity.Room;
import io.dume.dume.student.recordsPage.Record;
import io.dume.dume.util.DumeUtils;

public class Google {
    private String currentRoom = null;
    private static Google instance = null;
    private List<String> roomIdList = null;
    private List<Room> rooms;
    private List<Record> recordList;
    private List<DocumentSnapshot> records;
    private String accountPrefix;
    private String accountMajor;


    public String getAccountPrefix() {
        return accountPrefix;
    }

    public void setAccountPrefix(String accountPrefix) {
        this.accountPrefix = accountPrefix;
    }

    public String getAccountMajor() {


        return accountMajor;
    }

    public void setAccountMajor(String accountMajor) {
        if (accountMajor.equals(DumeUtils.TEACHER)) {
            setAccountPrefix("T_");
        } else if (accountMajor.equals(DumeUtils.STUDENT)) {
            setAccountPrefix("S_");
        } else {
            setAccountPrefix("B_");
        }
        this.accountMajor = accountMajor;
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public static Google getInstance() {
        if (instance == null) {
            instance = new Google();
        }
        return instance;
    }

    public List<String> getRoomIdList() {
        return roomIdList;
    }

    public void setRoomIdList(List<String> roomIdList) {
        this.roomIdList = roomIdList;
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }

    public List<DocumentSnapshot> getRecords() {
        return records;
    }

    public void setRecords(List<DocumentSnapshot> records) {
        this.records = records;
    }
}
