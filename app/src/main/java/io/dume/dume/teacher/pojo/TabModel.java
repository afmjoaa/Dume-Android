package io.dume.dume.teacher.pojo;

import androidx.annotation.Keep;

import java.io.Serializable;
@Keep
public class TabModel implements Serializable {
   private int  normalIcon;
    private int selectedIcon;
    private int badge;
   private String tabName;

    public int getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public TabModel(int normalIcon, int selectedIcon, int badge, String tabName) {
        this.normalIcon = normalIcon;
        this.selectedIcon = selectedIcon;
        this.badge = badge;
        this.tabName = tabName;
    }



    public int getNormalIcon() {
        return normalIcon;
    }

    public void setNormalIcon(int normalIcon) {
        this.normalIcon = normalIcon;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }
}
