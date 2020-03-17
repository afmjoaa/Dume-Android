package io.dume.dume.commonActivity.foreignObligation;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;

import io.dume.dume.teacher.homepage.TeacherContract;

public interface PayContact {

    interface View {

        void findView();

        void initPayActivity();

        void configPayActivity();

        void signOutAndLoginPrevious();

        void signOut();

        void flush(String msg);

        void noObligationFound();

        void obligationFound();

        void setDocumentSnapshot(DocumentSnapshot documentSnapshot);

        void hideProgress();

        void showProgress();
    }

    interface Presenter {

        void payActivityEnqueue();

        void onPayActivityViewIntracted(android.view.View view);

    }

    interface Model {

        void setInstance(PayModel mModel);

        void payActivityHawwa();

        void addShapShotListener(EventListener<DocumentSnapshot> updateViewListener);

        Task<DocumentSnapshot> getResultSnapShot(TeacherContract.Model.Listener<DocumentSnapshot> listener);
    }
}
