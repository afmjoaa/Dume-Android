package io.dume.dume.common.chatActivity;

public interface ChatActivityContact {

    interface View {

        void findView();

        void initChat();

        void configChat();

    }

    interface Presenter {

        void chatEnqueue();

        void onChatViewIntracted(android.view.View view);

    }

    interface Model {

        void chatHawwa();
    }
}
