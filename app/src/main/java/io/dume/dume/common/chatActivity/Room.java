package io.dume.dume.common.chatActivity;

import androidx.annotation.Keep;

import java.util.Date;

@Keep
public class Room {
    String roomId;
    String opponentUid;
    String opponentDP;
    String opponentName;
    boolean mute;
    String unreadMsgString;
    Date lastMsgTime;
    Integer unreadMsg;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public Room(String roomId,String opponentUid, String opponentDP, String opponentName, Date lastMsgTime, boolean mute,  String unreadMsgString, Integer unreadMsg) {
        this.roomId = roomId;
        this.opponentUid = opponentUid;
        this.opponentDP = opponentDP;
        this.opponentName = opponentName;
        this.lastMsgTime = lastMsgTime;
        this.mute = mute;
        this.unreadMsgString= unreadMsgString;
        this.unreadMsg = unreadMsg;
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

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public String getUnreadMsgString() {
        return unreadMsgString;
    }

    public void setUnreadMsgString(String unreadMsgString) {
        this.unreadMsgString = unreadMsgString;
    }

    public Date getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(Date lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public Integer getUnreadMsg() {
        return unreadMsg;
    }

    public void setUnreadMsg(Integer unreadMsg) {
        this.unreadMsg = unreadMsg;
    }

}
