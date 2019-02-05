package io.dume.dume;

import java.util.List;

import io.dume.dume.common.chatActivity.Room;

public class Google {
    private String currentRoom = null;
    private static Google instance = null;
    private List<String> roomIdList = null;
    private List<Room> rooms;

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
}
