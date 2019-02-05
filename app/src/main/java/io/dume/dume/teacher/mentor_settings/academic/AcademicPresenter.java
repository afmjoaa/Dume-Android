package io.dume.dume.teacher.mentor_settings.academic;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

import static io.dume.dume.util.DumeUtils.hideKeyboard;

public class AcademicPresenter implements AcademicContract.Presenter, AcademicContract.Model.ModelCallback {
    private final Context context;
    private final Activity activity;
    AcademicContract.View view;
    AcademicContract.Model model;

    AcademicPresenter(Context context, AcademicContract.View view, AcademicContract.Model model) {
        this.view = view;
        this.model = model;
        this.model.attachCallback(this);
        this.context = context;
        this.activity = (Activity) context;
    }

    @Override
    public void enqueue() {
        view.findView();
        view.configActivity();
        view.getBundle();
    }

    @Override
    public void onViewIntracted(View element) {
        switch (element.getId()) {
            case R.id.fabEdit:
                view.enableLoad();
                if (view.getAction().equals("edit") || view.getAction().equals("add") || view.getAction().equals("c_add")) {
                    if (validateInput(view.getValidationCheck())) {
                        model.syncWithDatabase(view.getLevel(), view.getInstitution(), view.getDegree(), view.getStartYear(), view.getEndYear(), view.getRestult());
                    } else {
                        view.toast("Required fields are empty");
                        view.disableLoad();
                    }
                } else view.toast("Internal Error");
                break;
            case R.id.input_level:
                view.selectLevelClicked();
                break;
            case R.id.institutionET:
                break;
            case R.id.degreeTV:
                break;
            case R.id.fromET:
                view.selectFromClicked();
                break;
            case R.id.toET:
                view.selectToClicked();
                break;
            case R.id.levelET:
                view.selectResultClicked();
                break;
            default:
                break;
        }
    }

    private boolean validateInput(boolean identify) {
        boolean validate = false;
        if (identify) {
            if (!view.getLevel().equals("") && !view.getInstitution().equals("") && !view.getDegree().equals("") && !view.getStartYear().equals("") && !view.getEndYear().equals("") && !view.getRestult().equals("")) {
                validate = true;
            } else {
                if (view.getLevel().equals("")) {
                    view.inValidFound("level");
                }
                if (view.getInstitution().equals("")) {
                    view.inValidFound("institution");
                }
                if (view.getDegree().equals("")) {
                    view.inValidFound("degree");
                }
                if (view.getStartYear().equals("")) {
                    view.inValidFound("from");
                }
                if (view.getEndYear().equals("")) {
                    view.inValidFound("to");
                }
                if (view.getRestult().equals("")) {
                    view.inValidFound("result");
                }
            }
        } else {
            if (!view.getLevel().equals("") && !view.getInstitution().equals("") && !view.getDegree().equals("") && !view.getStartYear().equals("") && !view.getRestult().equals("")) {
                validate = true;
            } else {
                if (view.getLevel().equals("")) {
                    view.inValidFound("level");
                }
                if (view.getInstitution().equals("")) {
                    view.inValidFound("institution");
                }
                if (view.getDegree().equals("")) {
                    view.inValidFound("degree");
                }
                if (view.getStartYear().equals("")) {
                    view.inValidFound("from");
                }
                if (view.getRestult().equals("")) {
                    view.inValidFound("result");
                }
            }
        }

        return validate;

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess() {
        if (view.getAction().equals("add") || view.getAction().equals("c_add")) {
            hideKeyboard(activity);
            view.toast("Qualification Added Successfully");
            view.gotoBackActivity();
            //view.disableLoad();
        } else {
            hideKeyboard(activity);
            view.toast("Qualification Edited Successfully");
            view.gotoBackActivity();
            //view.disableLoad();
        }
    }

    @Override
    public void onFail(String error) {
        view.toast(error);
        view.disableLoad();
    }
}
