package io.dume.dume.student.recordsRejected;

import android.content.Context;

import io.dume.dume.student.recordsPage.RecordsPageModel;

public class RecordsRejectedModel extends RecordsPageModel implements RecordsRejectedContract.Model {

    private final Context context;

    public RecordsRejectedModel(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public void recordsRejectedHawwa() {

    }
}
