package io.dume.dume.common.chatActivity;

public class Room {
    String roomId;
    String opponentUid;
    String opponentDP;
    String opponentName;
    String lastActiveTime;
    boolean mute;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Room(String roomId,String opponentUid, String opponentDP, String opponentName, String lastActiveTime, boolean mute) {
        this.roomId = roomId;
        this.opponentUid = opponentUid;
        this.opponentDP = opponentDP;
        this.opponentName = opponentName;
        this.lastActiveTime = lastActiveTime;
        this.mute = mute;
    }

    public String getOpponentUid() {
        return opponentUid;
    }

    public void setOpponentUid(String opponentUid) {
        this.opponentUid = opponentUid;
    }

    public String getOpponentDP() {
        return opponentDP;
    }

    public void setOpponentDP(String opponentDP) {
        this.opponentDP = opponentDP;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public void setOpponentName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(String lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }
}
