package io.dume.dume.model;

import java.util.ArrayList;

import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Skill;

public interface TeacherModel {
    void saveSkill(Skill skill, TeacherContract.Model.Listener<Void> listener);

}
