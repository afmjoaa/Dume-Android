package io.dume.dume.student.grabingLocation;

import androidx.annotation.Keep;

import com.google.firebase.firestore.GeoPoint;

@Keep
public class MenualRecyclerData {
    String primaryText;
    String secondaryText;
    GeoPoint location;
    String identify;

    public MenualRecyclerData(String primaryText, String secondaryText, GeoPoint location, String identify) {
        this.primaryText = primaryText;
        this.secondaryText = secondaryText;
        this.location = location;
        this.identify = identify;
    }

    public MenualRecyclerData() {
    }

    public String getPrimaryText() {
        return primaryText;
    }

    public void setPrimaryText(String primaryText) {
        this.primaryText = primaryText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }
}
