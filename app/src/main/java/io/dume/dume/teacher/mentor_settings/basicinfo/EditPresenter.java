package io.dume.dume.teacher.mentor_settings.basicinfo;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import io.dume.dume.R;

public class EditPresenter implements EditContract.Presenter, EditContract.onDataUpdate {
    private EditContract.View view;
    private EditContract.Model model;
    private Uri imageUri;
    private static final String TAG = "EditPresenter";

    public EditPresenter(EditContract.View view, EditContract.Model model) {
        this.view = view;
        this.model = model;
        model.setListener(this);
    }

    @Override
    public void enqueue() {
        view.configureView();
        view.configureCallback();
    }

    @Override
    public void onClick(View element) {
        switch (element.getId()) {
            case R.id.fabEdit:
                view.enableLoad();
                model.synWithDataBase(view.firstName(), view.lastName(), view.getAvatarUrl(), view.gmail(), view.gender(), view.phone(), view.religion(), view.maritalStatus());
                break;

            case R.id.profileImage:
                view.updateImage();
                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == EditAccount.RESULT_OK) {
                    imageUri = data.getData();
                    view.setImage(imageUri);
                }
                break;
            case 2:


                break;
        }

        if (imageUri != null) {
            view.enableLoad();
            model.uploadImage(imageUri, new EditContract.DownloadListener() {
                @Override
                public void onSuccess(String url) {
                    view.setAvatarUrl(url);
                    view.disableLoad();
                }

                @Override
                public void onFail(String msg) {
                    view.toast(msg);
                    view.disableLoad();
                }
            });
        }

    }


    @Override
    public void onUpdateSuccess() {
        view.disableLoad();
        view.snakbar("Profile Updated Successfully");
    }

    @Override
    public void onFail(String error) {
        view.toast(error);
    }
}
