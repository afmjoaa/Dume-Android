package io.dume.dume.student.heatMap;

import androidx.annotation.Keep;

@Keep
public class AccountRecyData {
    public String accouName;
    public int iconId;
    public int selectedOne;

    public String getAccouName() {
        return accouName;
    }

    public void setAccouName(String accouName) {
        this.accouName = accouName;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getSelectedOne() {
        return selectedOne;
    }

    public void setSelectedOne(int selectedOne) {
        this.selectedOne = selectedOne;
    }
}
