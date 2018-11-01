package io.dume.dume.teacher.skill;

import android.content.Intent;

import io.dume.dume.util.BaseContract;

public interface SkillContract {
    interface View extends BaseContract.View {
        void goToCrudActivity(String action);
    }

    interface Model extends BaseContract.Model {

    }

    interface Presenter extends BaseContract.Presenter {

    }
}
