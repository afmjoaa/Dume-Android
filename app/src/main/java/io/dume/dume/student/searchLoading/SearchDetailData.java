package io.dume.dume.student.searchLoading;

import android.graphics.Bitmap;
import androidx.annotation.Keep;

@Keep
public class SearchDetailData {
    private String itemName;
    private String itemInfo;
    private String itemChange;
    private Bitmap imageSrc;

    public SearchDetailData(String itemName, String itemInfo, String itemChange, Bitmap imageSrc) {
        this.itemName = itemName;
        this.itemInfo = itemInfo;
        this.itemChange = itemChange;
        this.imageSrc = imageSrc;
    }

    public SearchDetailData() {
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

    public String getItemChange() {
        return itemChange;
    }

    public void setItemChange(String itemChange) {
        this.itemChange = itemChange;
    }

    public Bitmap getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(Bitmap imageSrc) {
        this.imageSrc = imageSrc;
    }
}
