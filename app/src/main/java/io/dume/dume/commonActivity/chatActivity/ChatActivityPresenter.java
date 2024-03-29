package io.dume.dume.commonActivity.chatActivity;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

public class ChatActivityPresenter implements ChatActivityContact.Presenter {

    private ChatActivityContact.View mView;
    private ChatActivityContact.Model mModel;
    private Context context;
    private Activity activity;

    public ChatActivityPresenter(Context context, ChatActivityContact.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (ChatActivityContact.View) context;
        this.mModel = mModel;
    }

    @Override
    public void chatEnqueue() {
        mView.findView();
        mView.initChat();
        mView.configChat();
    }

    @Override
    public void onChatViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.attachment_document_image:
                //mView.showDialogue();
                mView.comingSoon();
                break;
            case R.id.attachment_audio_image:
                mView.comingSoon();
                break;
            case R.id.attachment_camera_image:
                mView.comingSoon();
                break;
            case R.id.attachment_gallery_image:
                mView.comingSoon();
                break;
            case R.id.attachment_location_image:
                mView.comingSoon();
                break;
            case R.id.view_musk:
                mView.viewMuskClicked();
                break;

        }
    }
}
