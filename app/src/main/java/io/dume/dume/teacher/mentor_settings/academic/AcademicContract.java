package io.dume.dume.teacher.mentor_settings.academic;

public interface AcademicContract {
    interface View {
        void toast(String msg);

        void snak(String msg);

        String getInstitution();

        String getStartYear();

        String getEndYear();

        String getDescription();

        void getBundle();

        boolean isGraduate();

        void configView();

        void setListener();


        String getDegree();

        void enableLoad();

        void disableLoad();

        void goBack();

        String getItemUid();

        String getAction();

    }

    interface Model {
        void syncWithDatabase(String itemUID, String school, String degree, String from, String to, String description);

        void addToDatabase(String school, String degree, String from, String to, String description);

        void attachCallback(ModelCallback listener);

        void removeFromDatabase(String itemUid, ModelCallback modelCallback);

        void detachFirebaseListener();

        interface ModelCallback {
            void onStart();

            void onSuccess();

            void onFail(String error);
        }

    }

    interface Presenter {
        void enqueue();

        void onViewIntracted(android.view.View view);


    }
}