package io.dume.dume.teacher.mentor_settings;

import java.util.ArrayList;

public class AccountSettingsModel implements AccountSettingsContract.MentorModel {
    @Override
    public int getData() {
        return 0;
    }

    @Override
    public void getDataArray(DataListener listener) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Enam");
        arrayList.add("Joaa");
        arrayList.add("Zoya");
        arrayList.add("Doe");
        arrayList.add("Edward");
        arrayList.add("Alex");
        arrayList.add("Enam");
        arrayList.add("Joaa");
        arrayList.add("Zoya");
        arrayList.add("Doe");
        arrayList.add("Edward");
        arrayList.add("Alex");
        arrayList.add("Enam");
        arrayList.add("Joaa");
        arrayList.add("Zoya");
        arrayList.add("Doe");
        arrayList.add("Edward");
        arrayList.add("Alex");
        arrayList.add("Enam");
        arrayList.add("Joaa");
        arrayList.add("Zoya");
        arrayList.add("Doe");
        arrayList.add("Edward");
        arrayList.add("Alex");
        arrayList.add("Enam");
        arrayList.add("Joaa");
        arrayList.add("Zoya");
        arrayList.add("Doe");
        arrayList.add("Edward");
        arrayList.add("Alex");
        arrayList.add("Enam");
        arrayList.add("Joaa");
        arrayList.add("Zoya");
        arrayList.add("Doe");
        arrayList.add("Edward");
        arrayList.add("Alex");
        new android.os.Handler().postDelayed(() -> {
            listener.onSuccess(arrayList);
        }, 1000);

    }


}
