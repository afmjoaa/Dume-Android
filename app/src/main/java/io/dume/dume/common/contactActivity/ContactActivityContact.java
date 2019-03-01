package io.dume.dume.common.contactActivity;

import java.util.List;

import io.dume.dume.teacher.homepage.TeacherContract;

public interface ContactActivityContact {

    interface View {

        void findView();

        void initContactActivity();

        void configContactActivity();

        void flush(String toFlush);

        void loadRV(List<ContactData> contactDialogueData);

    }

    interface Presenter {

        void contactActivityEnqueue();

        void onContactActivityViewIntracted(android.view.View view);

    }

    interface Model {

        void contactActivityHawwa();

        void readContact(String accountMajor, TeacherContract.Model.Listener<List<ContactData>> listener);
    }
}
