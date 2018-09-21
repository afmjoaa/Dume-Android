package io.dume.dume.teacher.mentor_settings.basicinfo;

import android.content.Intent;
import android.net.Uri;

public class EditContract {
    interface View {
        void configureView();

        void configureCallback();

        void snakbar(String msg);

        void toast(String toast);

        String firstName();

        String lastName();

        String maritalStatus();

        String gmail();

        String religion();

        String gender();

        String phone();

        void updateImage();

        void setImage(Uri uri);

        void setAvatarUrl(String url);

        String getAvatarUrl();

        void enableLoad();

        void disableLoad();

    }

    interface Presenter {
        void enqueue();

        void onClick(android.view.View view);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

    interface Model {
        void synWithDataBase(String first, String last, String avaterUrl, String email, String gender, String phone, String religion, String marital);

        void setListener(onDataUpdate listener);

        void uploadImage(Uri uri, DownloadListener progressListener);
    }

    interface onDataUpdate {
        void onUpdateSuccess();

        void onFail(String error);

    }

    interface DownloadListener {
        void onSuccess(String url);

        void onFail(String msg);
    }
}