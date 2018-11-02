package io.dume.dume.student.mentorAddvertise;

import android.app.Activity;
import android.content.Context;
import android.view.View;

public class MentorAddvertisePresenter implements MentorAddvertiseContact.Presenter {
    private MentorAddvertiseContact.View mView;
    private MentorAddvertiseContact.Model mModel;
    private Context context;
    private Activity activity;

    public MentorAddvertisePresenter(Context context, MentorAddvertiseContact.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (MentorAddvertiseContact.View) context;
        this.mModel = mModel;
    }

    @Override
    public void mentorAddvertiseEnqueue() {
        mView.findView();
        mView.initMentorAddvertise();
        mView.configMentorAddvertise();
    }

    @Override
    public void onMentorAddvertiseViewIntracted(View view) {

    }
}
