package io.dume.dume.student.heatMap;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import io.dume.dume.R;

public class HeatMapPresenter implements HeatMapContract.Presenter {

    private HeatMapContract.View mView;
    private HeatMapContract.Model mModel;
    private Context context;
    private Activity activity;

    public HeatMapPresenter(Context context, HeatMapContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (HeatMapContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void heatMapEnqueue() {
        mView.findView();
        mView.initHeatMap();
        mView.configHeatMap();

        /*
         fireStore.collection("app").document("dume_utils").get().addOnSuccessListener(documentSnapshot -> {
            Log.w(TAG, "hasUpdate: ");
            Number currentVersion = (Number) documentSnapshot.get("version_code");
            String updateVersionName = (String) documentSnapshot.get("version_name");
            String updateDescription = (String) documentSnapshot.get("version_description");
            Number totalStudent = (Number) documentSnapshot.get("total_students");
            Number totalMentors = (Number) documentSnapshot.get("total_mentors");
            Google.getInstance().setTotalStudent(totalStudent == null ? 0 : totalStudent.intValue());
            Google.getInstance().setTotalMentor(totalMentors == null ? 0 : totalMentors.intValue());
            if (currentVersion != null) {
                if (currentVersion.intValue() > BuildConfig.VERSION_CODE) {
                    SplashActivity.updateVersionName = updateVersionName;
                    SplashActivity.updateDescription = updateDescription;
                    listener.onSuccess(true);
                } else {
                    listener.onSuccess(false);
                }
            } else listener.onSuccess(false);

        }).addOnFailureListener(e -> listener.onError(e.getLocalizedMessage()));
       */

    }

    @Override
    public void onHeatMapViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.view_musk:
                mView.viewMuskClicked();
                break;
            case R.id.fab:
                mView.onCenterCurrentLocation();
                break;
        }
    }
}
