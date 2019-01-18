package io.dume.dume.teacher.mentor_settings.academic;

import android.view.View;

import io.dume.dume.R;

public class AcademicPresenter implements AcademicContract.Presenter, AcademicContract.Model.ModelCallback {
    AcademicContract.View view;
    AcademicContract.Model model;

    AcademicPresenter(AcademicContract.View view, AcademicContract.Model model) {
        this.view = view;
        this.model = model;
        this.model.attachCallback(this);
    }

    @Override
    public void enqueue() {
        view.configView();
        view.getBundle();
        view.setListener();
    }

    @Override
    public void onViewIntracted(View element) {
        switch (element.getId()) {
            case R.id.fabEdit:
               if (view.getAction().equals("edit")||view.getAction().equals("add")) {
                    if (validateInput()) {
                        model.syncWithDatabase( view.getInstitution(), view.getDegree(), view.getStartYear(), view.getEndYear(), view.getDescription(),view.getRestult(),view.getResultType());
                    }
                    else view.toast("Required fields are empty");
                } else view.toast("Internal Error");
                break;
            case R.id.deleteButtonHeader:

                break;
            default:
        }
    }

    private boolean validateInput() {
        boolean validate = false;
        if (!view.getInstitution().equals("") && !view.getStartYear().equals("From") && !view.getEndYear().equals("To")) {
            validate = true;
        }
        return validate;

    }

    @Override
    public void onStart() {
        view.enableLoad();
    }

    @Override
    public void onSuccess() {
        if (view.getAction().equals("add")) {
            view.disableLoad();
            view.snak("Education Added Successfully");

        }
        if (view.getAction().equals("edit")) {
            view.disableLoad();
            view.snak("Education Edited Successfully");
        }


    }

    @Override
    public void onFail(String error) {
        view.disableLoad();
        view.toast(error);
    }
}
