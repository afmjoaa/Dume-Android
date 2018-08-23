package io.dume.dume.util;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.dume.dume.R;

public class Datastore {
    Context context;
    private List<String> list;

    public Datastore(Context context) {
        this.context = context;
    }

    List<String> getClassList(String name) {

        if (name.equals("Bangla")) {
            String[] stringArray = context.getResources().getStringArray(R.array.BanglaMedium);
            list = Arrays.asList(stringArray);
        } else if (name.equals("English")) {
            String[] stringArray = context.getResources().getStringArray(R.array.EnglishMedium);
            list = Arrays.asList(stringArray);
        }

        return list;
    }


}
