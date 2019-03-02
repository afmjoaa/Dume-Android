package io.dume.dume.student.pojo;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public  class DataSet {
    private ArrayList<LatLng> mDataset;
    private String mUrl;

    public DataSet()
    {

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

    public DataSet(ArrayList<LatLng> dataSet, String url) {
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