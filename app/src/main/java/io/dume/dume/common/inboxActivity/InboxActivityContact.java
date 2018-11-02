package io.dume.dume.common.inboxActivity;

public interface InboxActivityContact {

    interface View {

        void findView();

        void initInbox();

        void configInbox();

    }

    interface Presenter {

        void inboxEnqueue();

        void onInboxViewIntracted(android.view.View view);

    }

    interface Model {

        void inboxHawwa();
    }
}
