package io.dume.dume.student.pojo;

import androidx.annotation.Keep;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

@Keep
public class HeatMapDataSet {
    private ArrayList<LatLng> mDataset;
    private String mUrl;

    public HeatMapDataSet() {

    }

    public ArrayList<LatLng> getmDataset() {
        return mDataset;
    }

    public void setmDataset(ArrayList<LatLng> mDataset) {
        this.mDataset = mDataset;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public HeatMapDataSet(ArrayList<LatLng> dataSet, String url) {
        this.mDataset = dataSet;
        this.mUrl = url;
    }

    public ArrayList<LatLng> getData() {
        return mDataset;
    }

    public String getUrl() {
        return mUrl;
    }
}