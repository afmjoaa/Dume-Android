package io.dume.dume.teacher.addskill;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.dume.dume.R;
import io.dume.dume.teacher.BaseFragment;

public final class AddSkillFragment extends BaseFragment implements AddSkillContract.View {

    private AddSkillContract.Presenter mPresenter;


    public AddSkillFragment() {

    }

    public static AddSkillFragment newInstance() {
        return new AddSkillFragment();
    }

    @Override
    public void setPresenter(AddSkillContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_skill_layout, container, false);
    }

    @Override
    public void init() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void flush(String message) {

    }

    public interface OnAddSkillFragmentInteractionListener {

        void onAddSkillFragmentInteraction();
    }
}
