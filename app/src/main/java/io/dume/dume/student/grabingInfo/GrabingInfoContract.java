package io.dume.dume.student.grabingInfo;

import android.net.Uri;

import java.io.InputStream;

import io.dume.dume.inter_face.usefulListeners;

public interface GrabingInfoContract {
    interface View {

        void configGrabingInfoPage();

        void initGrabingInfoPage();

        void findView();

        void viewMuskClicked();

        void fabClicked(android.view.View view);

        String getContactAvatarUri();

        void setContactAvatar(String uri);

        void selectFromContactClicked();

        void firstContactClicked();

        void secondContactClicked();
    }

    interface Presenter {

        void grabingInfoPageEnqueue();

        void onGrabingInfoViewIntracted(android.view.View view);

    }

    interface Model {

        void grabingInfoPagehawwa();

        void uploadImage(InputStream uri, usefulListeners.uploadToSTGListererMin progressListener, String ref);
    }
}
