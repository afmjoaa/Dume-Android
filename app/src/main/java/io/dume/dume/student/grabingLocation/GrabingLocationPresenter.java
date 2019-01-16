package io.dume.dume.student.grabingLocation;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.Map;
import java.util.Objects;

import io.dume.dume.R;
import io.dume.dume.teacher.homepage.TeacherContract;

public class GrabingLocationPresenter implements GrabingLocaitonContract.Presenter {

    private GrabingLocaitonContract.View mView;
    private GrabingLocaitonContract.Model mModel;
    private Context context;
    private Activity activity;
    private static final String TAG = "GrabingLocationPresente";

    public GrabingLocationPresenter(Context context, GrabingLocaitonContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = (GrabingLocaitonContract.View) context;
        this.mModel = mModel;
    }

    @Override
    public void grabingLocationPageEnqueue() {
        mView.findView();
        mView.initGrabingLocationPage();
        mView.makingCallbackInterfaces();
        mView.configGrabingLocationPage();
        mView.retriveSavedData();

        /*mModel.addShapShotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                final GeoPoint current_address = documentSnapshot.getGeoPoint("current_address");
                if (current_address != null) {
                    if (Objects.requireNonNull(current_address).getLatitude() != 84.9 && current_address.getLongitude() != 180) {
                        mView.setCurrentAddress(current_address);
                    }
                }

            } else {
                mView.flush("Does not found any user");
                Log.w(TAG, "onAccountTypeFound: document is not null");
            }
        });*/
    }

    @Override
    public void onGrabingLocationViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.fab:
                mView.onCenterCurrentLocation();
                break;
            case R.id.discard_image:
                mView.onDiscardSearchClicked();
                break;
            case R.id.location_done_btn:
                mView.onLocationDoneBtnClicked();
                break;
            case R.id.hack_set_location_on_map:
                mView.hackSetLocaOnMapClicked();


        }
    }

    @Override
    public void retriveSavedPlacesData(TeacherContract.Model.Listener<DocumentSnapshot> listener) {
        mModel.addShapShotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null){
                    listener.onSuccess(documentSnapshot);
                }else{
                    listener.onError(e.getMessage().toString());
                }
            }
        });
    }
}
