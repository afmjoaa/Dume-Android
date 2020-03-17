package io.dume.dume.commonActivity.inboxActivity;

import androidx.annotation.Keep;

import java.net.URL;

@Keep
public class InboxChatData {
    URL chatUserDP;
    URL groupUserDP;
    String chatUserName;
    String lastText;
    String deliveryTime;
    int unreadCount;
    boolean mute;
    boolean online;

}
