package io.dume.dume.teacher.skill;

import java.util.ArrayList;

import io.dume.dume.teacher.pojo.Skill;
import io.dume.dume.util.MyBaseContract;

public interface SkillContract {
    interface View extends MyBaseContract.View {
        void goToCrudActivity(String action);

        void loadSkillRV(ArrayList<Skill> list);

        void performMultiFabClick();
    }

    interface Model extends MyBaseContract.Model{

    }

    interface Presenter extends MyBaseContract.Presenter {

    }
}
