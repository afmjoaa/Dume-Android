package io.dume.dume.teacher.mentor_settings.academic;

import android.view.View;

import io.dume.dume.R;

public class AcademicPresenter implements AcademicContract.Presenter {
    AcademicContract.View view;
    AcademicContract.Model model;

    AcademicPresenter(AcademicContract.View view, AcademicContract.Model model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void enqueue() {
        view.configView();
        view.setListener();
    }

    @Override
    public void onViewIntracted(View element) {
        switch (element.getId()) {
            case R.id.fabEdit:
                if (validateInput()) {
                    if (view.getFlag() == 0) {
                        AcademicContract.Model.ModelCallback listener = new AcademicContract.Model.ModelCallback() {
                            @Override
                            public void onStart() {
                                view.enableLoad();
                            }

                            @Override
                            public void onSuccess() {
                                view.disableLoad();
                                view.snak("Education Added Successfully");

                            }

                            @Override
                            public void onFail(String error) {
                                view.disableLoad();
                                view.toast(error);
                            }
                        };
                        model.attachCallback(listener);
                        model.addToDatabase(view.getInstitution(), view.getDegree(), view.getStartYear(), view.getEndYear(), view.getDescription());
                    } else if (view.getFlag() == 1) {


                    } else view.toast("Internal Error. Try Again");
                } else {
                    view.toast("Invalidate Input");
                }

                break;
            default:
        }
    }

    private boolean validateInput() {
        boolean validate = false;
        if (!view.getInstitution().isEmpty() || !view.getStartYear().equals("From") || !view.getEndYear().equals("To")) {
            validate = true;
        }
        return validate;

    }
}
