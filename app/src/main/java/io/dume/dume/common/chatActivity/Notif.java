package io.dume.dume.common.chatActivity;

class Notif {
    String title;
    String uid;
    String reason;
    String token;
    String name;

    public Notif(String name, String title, String uid, String reason, String token) {
        this.name = name;
        this.title = title;
        this.uid = uid;
        this.reason = reason;
        this.token = token;

    }
}
