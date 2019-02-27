package io.dume.dume.common.inboxActivity;

import java.io.Serializable;
import java.util.Date;

public class InboxNotiData implements Serializable {
    public static String DUME_NOTIFICATION = "global",
            NEW_DUME_REQUEST = "new_request",
            REQUEST_ACCEPTED = "request_accepted",
            REQUEST_REJECTED = "request_rejected",
            PAYMENT_SUCCESS = "p_success",
            PAYMENT_FAILED = "p_failed",
            WARNING = "warning",
            NEW_ROOM_STARTED = "new_room";

    String name;
    String title;
    String body;
    boolean seen;
    String type;
    String avatar;
    String mail;
    Date date;




}
