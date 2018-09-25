package io.dume.dume.common.chatActivity;

import android.app.Activity;
import android.content.Context;
import android.view.View;

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

    }
}
