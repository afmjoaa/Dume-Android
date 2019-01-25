package io.dume.dume.teacher.mentor_settings.academic;

public interface AcademicContract {
    interface View {
        void toast(String msg);

        void snak(String msg);

        void getBundle();

        void configActivity();

        String getDegree();

        String getRestult();

        String getInstitution();

        String getStartYear();

        String getEndYear();

        String getLevel();

        void enableLoad();

        void disableLoad();

        String getAction();

        void selectLevelClicked();

        void selectFromClicked();

        void selectToClicked();

        void selectResultClicked();

        void findView();

        void inValidFound(String identify);

        void clearAllField();
    }

    interface Model {
        void syncWithDatabase( String level, String institution, String degree, String from, String to, String result);

        void attachCallback(ModelCallback listener);

        void removeFromDatabase(String degree, ModelCallback modelCallback);

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
