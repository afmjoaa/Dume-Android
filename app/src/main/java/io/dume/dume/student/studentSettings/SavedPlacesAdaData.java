package io.dume.dume.student.studentSettings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

@JsonIgnoreProperties
public class SavedPlacesAdaData implements Serializable {

    @JsonProperty("primary_text")
    String primary_text;
    @JsonProperty("secondary_text")
    String secondary_text;
    @JsonProperty("location")
    GeoPoint location;

    public SavedPlacesAdaData( @JsonProperty("primary_text")String primary_text,  @JsonProperty("secondary_text")String secondary_text,   @JsonProperty("location")GeoPoint location) {
        this.primary_text = primary_text;
        this.secondary_text = secondary_text;
        this.location = location;
    }

    public SavedPlacesAdaData() {
    }

    @JsonProperty("primary_text")
    public String getPrimary_text() {
        return primary_text;
    }

    public void setPrimary_text(String primary_text) {
        this.primary_text = primary_text;
    }

    @JsonProperty("secondary_text")
    public String getSecondary_text() {
        return secondary_text;
    }

    public void setSecondary_text(String secondary_text) {
        this.secondary_text = secondary_text;
    }

    @JsonProperty("location")
    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
