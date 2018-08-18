package io.dume.dume.student.homepage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import io.dume.dume.R;

public class StudentPresenter implements StudentContract.Presenter {

    private static final String TAG = "MapActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private Context context;
    private StudentContract.View mView;
    private StudentContract.Model model;
    private Activity activity;

    public StudentPresenter(Context context, StudentContract.View view, StudentContract.Model model) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = view;
        this.model = model;
    }

    @Override
    public void enqueue() {
        mView.configView();
    }

    @Override
    public void onViewIntracted(android.view.View view) {
        switch (view.getId()) {
            case R.id.signOutBtn:
                mView.onSignOut();
                break;
            case R.id.testingActivityBtn:
                if (isServicesOK()) {
                    mView.goToStudentHOmePage();
                }
                break;
            case R.id.testingMapBtn:
                if (isServicesOK()) {
                    mView.goToMapView();
                }
                break;
        }
    }

    public boolean isServicesOK() {
        Log.d(TAG, "isServicesOK: checking google services version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(activity);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(activity, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(context, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
