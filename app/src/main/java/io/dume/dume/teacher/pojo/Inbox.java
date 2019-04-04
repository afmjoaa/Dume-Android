package io.dume.dume.teacher.pojo;

import java.io.Serializable;

public class Inbox implements Serializable {
    private boolean unread;
    private String title;
    private int unreadNumber;

    public Inbox(boolean unread, String title, int unreadNumber) {
        this.unread = unread;
        this.title = title;
        this.unreadNumber = unreadNumber;
    }

    public boolean isUnread() {
        return unread;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getUnreadNumber() {
        return unreadNumber;
    }

    public void setUnreadNumber(int unreadNumber) {
        this.unreadNumber = unreadNumber;
    }
}
