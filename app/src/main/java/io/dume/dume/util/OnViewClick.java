package io.dume.dume.util;

import android.widget.CompoundButton;
import android.widget.RadioButton;

public interface OnViewClick {
    void onRadioButtonClick(CompoundButton view, int fragmentId , String levelName);
}
