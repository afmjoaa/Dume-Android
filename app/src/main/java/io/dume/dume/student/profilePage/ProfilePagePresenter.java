package io.dume.dume.student.profilePage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.Objects;

import javax.annotation.Nullable;

import io.dume.dume.R;

public class ProfilePagePresenter implements ProfilePageContract.Presenter {

    private ProfilePageContract.View mView;
    private ProfilePageContract.Model mModel;
    private Context context;
    private Activity activity;
    private static final String TAG = "ProfilePagePresenter";

    public ProfilePagePresenter(Context context, ProfilePageContract.View mView, ProfilePageContract.Model mModel) {
        this.context = context;
        this.activity = (Activity) context;
        this.mView = mView;
        this.mModel = mModel;
        mModel.setInstance((ProfilePageModel) mModel);
    }

    @Override
    public void profilePageEnqueue() {
        mView.findView();
        mView.initProfilePage();
        mView.configProfilePage();
        mModel.addShapShotListener((documentSnapshot, e) -> {
            if (documentSnapshot != null) {
                mView.setPhone(documentSnapshot.getString("phone_number"));
                String o = documentSnapshot.getString("last_name");
                mView.setLastName(o);
                String o1 = documentSnapshot.getString("first_name");
                mView.setFirstName(o1);
                mView.setUserName(o1, o);
                mView.setGmail(documentSnapshot.getString("email"));
                final GeoPoint current_address = documentSnapshot.getGeoPoint("current_address");
                if(Objects.requireNonNull(current_address).getLatitude() != 84.9 && current_address.getLongitude() != 180){
                    mView.setCurrentAddress(current_address);
                }
                mView.setCurrentStatus(documentSnapshot.getString("current_status"));
                mView.setPreviousResult(documentSnapshot.getString("previous_result"));
                mView.setGender(documentSnapshot.getString("gender"));
                mView.setProfileComPercent(documentSnapshot.getString("pro_com_%"));
                final String avatar = documentSnapshot.getString("avatar");
                if(avatar != null && !avatar.equals("")){
                    mView.setAvatar(avatar);
                }
            } else {
                mView.flush("Does not found any user");
                Log.w(TAG, "onAccountTypeFound: document is not null");
            }
        });

        //update the bundle data here if exist
        //& show a toast
        //mView.setResortedBundle();
    }

    @Override
    public void onProfileViewIntracted(View view) {
        switch (view.getId()) {
            case R.id.input_gerder:
                mView.onGenderClicked();
                break;
            case R.id.input_current_address:
                mView.onCurrentAddressClicked();
                break;
            case R.id.input_previous_result:
                mView.onPreviousResultClicked();
                break;
            case R.id.discard_imageview:
            case R.id.profile_discard_btn:
                mView.discardChangesClicked();
                break;
            case R.id.profile_update_btn:
            case R.id.done_imageview:
                mView.updateChangesClicked();
                break;
        }
    }

}
