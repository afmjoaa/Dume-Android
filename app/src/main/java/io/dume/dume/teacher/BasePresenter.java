package io.dume.dume.teacher;

import android.content.Context;
import android.support.annotation.NonNull;

public class BasePresenter {
    protected Context mContext;

    public void subscribe(@NonNull Context context) {
        this.mContext = context;
    }

    public boolean isSubscribed() {
        return mContext != null;
    }
}
