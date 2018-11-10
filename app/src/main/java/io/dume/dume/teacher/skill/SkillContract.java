package io.dume.dume.teacher.skill;

import io.dume.dume.util.MyBaseContract;

public interface SkillContract {
    interface View extends MyBaseContract.View {
        void goToCrudActivity(String action);
    }

    interface Model extends MyBaseContract.Model {

    }

    interface Presenter extends MyBaseContract.Presenter {

    }
}
