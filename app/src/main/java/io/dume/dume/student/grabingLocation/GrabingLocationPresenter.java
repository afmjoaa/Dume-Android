package io.dume.dume.student.grabingLocation;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.firebase.firestore.GeoPoint;

import java.util.Map;
import java.util.Objects;

import io.dume.dume.R;

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

        mModel.addShapShotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                final GeoPoint current_address = documentSnapshot.getGeoPoint("current_address");
                if (current_address != null) {
                    if (Objects.requireNonNull(current_address).getLatitude() != 84.9 && current_address.getLongitude() != 180) {
                        mView.setCurrentAddress(current_address);
                    }
                }
                /* mView.setDocumentSnapshot(documentSnapshot);
                final String avatar = documentSnapshot.getString("avatar");
                if (avatar != null && !avatar.equals("")) {
                    mView.setAvatar(avatar);
                }
                String o = documentSnapshot.getString("last_name");
                String o1 = documentSnapshot.getString("first_name");
                mView.setUserName(o1,o);
                mView.setMsgName(mView.generateMsgName(o1,o));
                mView.setRating((Map<String, Object>) documentSnapshot.get("self_rating"));

                mView.setUnreadMsg(documentSnapshot.getString("unread_msg"));
                mView.setUnreadNoti(documentSnapshot.getString("unread_noti"));
                mView.setUnreadRecords((Map<String, Object>) documentSnapshot.get("unread_records"));

                if (Objects.requireNonNull(documentSnapshot.getString("pro_com_%")).equals("100")) {
                    mView.setProfileComPercent(documentSnapshot.getString("pro_com_%"));
                }else{
                    mView.setProfileComPercent(documentSnapshot.getString("pro_com_%"));
                    mView.showSnackBar(documentSnapshot.getString("pro_com_%"));
                }*/

            } else {
                mView.flush("Does not found any user");
                Log.w(TAG, "onAccountTypeFound: document is not null");
            }
        });
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


        }


    }
}
