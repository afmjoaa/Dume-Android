package io.dume.dume.model;

import java.util.ArrayList;
import java.util.List;

import io.dume.dume.student.common.ReviewHighlightData;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.teacher.pojo.Skill;

public interface TeacherModel {
    void saveSkill(Skill skill, TeacherContract.Model.Listener<Void> listener);

    void getSkill(TeacherContract.Model.Listener<ArrayList<Skill>> listener);

    void switchAccountStatus(boolean status, TeacherContract.Model.Listener<Void> listener);

    void changeAllSkillStatus(ArrayList<Skill> skillArrayList, boolean status, TeacherContract.Model.Listener<Void> listener);

    void deleteSkill(String id, TeacherContract.Model.Listener<Void> listener);

    void loadReview(String id, String count, TeacherContract.Model.Listener<List<ReviewHighlightData>> listener);

    void swithSkillStatus(String id, boolean status, TeacherContract.Model.Listener<Void> listener);
}
