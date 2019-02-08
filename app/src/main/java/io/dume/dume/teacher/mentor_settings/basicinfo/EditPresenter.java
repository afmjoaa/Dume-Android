package io.dume.dume.teacher.mentor_settings.basicinfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import io.dume.dume.R;
import io.dume.dume.teacher.homepage.TeacherContract;
import io.dume.dume.util.DumeUtils;
import io.dume.dume.util.FileUtil;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static io.dume.dume.util.DumeUtils.getUserUID;
import static io.dume.dume.util.DumeUtils.hideKeyboard;

public class EditPresenter implements EditContract.Presenter, EditContract.onDataUpdate {
    private EditContract.View view;
    private EditContract.Model model;
    private final Context context;
    private final Activity activity;
    private Uri imageUri;
    private static final String TAG = "EditPresenter";
    private File compressedImage;
    private Uri selectedImageUri = null;
    private Uri outputFileUri;
    private File actualImage;

    public EditPresenter(Context context, EditContract.View view, EditContract.Model model) {
        this.view = view;
        this.model = model;
        this.context = context;
        this.activity = (Activity)context;
        model.setListener(this);
    }

    @Override
    public void enqueue() {
        view.configureView();
        view.configureCallback();
        view.enableLoad();
        model.getDocumentSnapShot(new TeacherContract.Model.Listener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot list) {
                view.onDataLoad(list);
                GeoPoint geoPoint = list.getGeoPoint("location");
                if (geoPoint != null) {
                    view.setCurrentAddress(geoPoint);
                }
                String profileComPercent = list.getString("pro_com_%");
                if (profileComPercent != null) {
                    if (profileComPercent.equals("100")) {
                        view.initProfileCompleteView();
                        view.setProfileComPercent(profileComPercent);
                    } else {
                        view.setProfileComPercent(profileComPercent);
                    }
                }
                Log.w(TAG, "onSuccess: Fucked Location");
                view.someThingChanged(false);
                view.disableLoad();
            }

            @Override
            public void onError(String msg) {
                view.toast(msg);
            }
        });
    }




    @Override
    public void onClick(View element) {
        switch (element.getId()) {
            case R.id.fabEdit:
                view.enableLoad();
                if (view.firstName().equals("") || view.lastName().equals("") || view.gmail().equals("") || view.getLocation().equals("")) {
                    if (view.firstName().equals("")) {
                        view.invalidFound("first");
                    }
                    if (view.lastName().equals("")) {
                        view.invalidFound("last");
                    }
                    if (view.gmail().equals("")) {
                        view.invalidFound("email");
                    }
                    if (view.getLocation().equals("")) {
                        view.invalidFound("location");
                    }
                    view.toast("Mandatory fields can't be kept empty.");
                    view.disableLoad();
                } else {
                    view.generatePercent();
                    if (view.getProfileComPercent().equals("100")) {
                        view.toast("Profile completed");
                    }
                    model.synWithDataBase(view.firstName(), view.lastName(), view.getAvatarUrl(), view.gmail(), view.gender(), view.phone(), view.religion(), view.maritalStatus(), view.getBirthDate(), view.getCurrentAddress(), view.getCurrentStatus(), view.getProfileComPercent());
                    view.disableLoad();
                }
                break;
            case R.id.profileImage:
                openImageIntent();
                break;
            case R.id.input_gender:
                view.onGenderClicked();
                break;
            case R.id.input_marital_status:
                view.onMaritalStatusClicked();
                break;
            case R.id.input_religion:
                view.onReligionClicked();
                break;
            case R.id.input_birth_date:
                view.onBirthDateClicked();
                break;
            case R.id.add_saved_place_layout:
                view.addQualifiaction();
                break;
            default:
        }
    }

    @Override
    public void fabClicked(TeacherContract.Model.Listener<Void> listener) {
        view.enableLoad();
        if (view.firstName().equals("") || view.lastName().equals("") || view.gmail().equals("") || view.getLocation().equals("")) {
            if (view.firstName().equals("")) {
                view.invalidFound("first");
            }
            if (view.lastName().equals("")) {
                view.invalidFound("last");
            }
            if (view.gmail().equals("")) {
                view.invalidFound("email");
            }
            if (view.getLocation().equals("")) {
                view.invalidFound("location");
            }
            view.toast("Mandatory fields can't be kept empty.");
            view.disableLoad();
        } else {
            view.generatePercent();
            if (view.getProfileComPercent().equals("100")) {
                view.toast("Profile completed");
            }
            model.synWithDataBase(view.firstName(), view.lastName(), view.getAvatarUrl(), view.gmail(), view.gender(), view.phone(), view.religion(), view.maritalStatus(), view.getBirthDate(), view.getCurrentAddress(), view.getCurrentStatus(), view.getProfileComPercent(), listener);
            view.disableLoad();
        }
    }

    @SuppressLint("CheckResult")
    private void compressImage(File actualImage) {
        view.setImage(Uri.fromFile(actualImage));
        new Compressor(view.getActivtiyContext())
                .compressToFileAsFlowable(actualImage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) {
                        compressedImage = file;
                        imageUri = Uri.fromFile(file);
                        view.setImage(imageUri);
                        if (imageUri != null) {
                            model.uploadImage(EditPresenter.this.imageUri, new EditContract.DownloadListener() {
                                @Override
                                public void onSuccess(String url) {
                                    view.setAvatarUrl(url);
                                    //view.setImage(url);
                                    view.disableLoad();
                                    view.generatePercent();
                                    view.someThingChanged(true);
                                }

                                @Override
                                public void onFail(String msg) {
                                    view.toast(msg);
                                    view.disableLoad();
                                }
                            });

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        throwable.printStackTrace();
                        view.toast(throwable.getLocalizedMessage());
                    }
                });
    }

    private void openImageIntent() {

        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "stu_" + getUserUID() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = view.getActivtiyContext().getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        Activity activtiyContext = (Activity) view.getActivtiyContext();
        activtiyContext.startActivityForResult(chooserIntent, DumeUtils.GALLARY_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == EditAccount.RESULT_OK) {
                    view.enableLoad();

                    final boolean isCamera;
                    if (data == null) {
                        isCamera = true;
                    } else {
                        final String action = data.getAction();
                        if (action == null) {
                            isCamera = false;
                        } else {
                            isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        }
                    }

                    if (isCamera) {
                        selectedImageUri = outputFileUri;
                    } else {
                        selectedImageUri = data == null ? null : data.getData();
                    }
                    if (selectedImageUri != null) {

                        try {
                            actualImage = FileUtil.from(view.getActivtiyContext(), selectedImageUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        compressImage(actualImage);
                    }
                }

                break;
            case 2:
                if (resultCode == EditAccount.RESULT_OK) {
                    LatLng selectedLocation = data.getParcelableExtra("selected_location");
                    if (selectedLocation != null) {
                        GeoPoint retrivedLocation = new GeoPoint(selectedLocation.latitude, selectedLocation.longitude);
                        view.setCurrentAddress(retrivedLocation);
                    }
                }
                break;
            case 1234:
                if (resultCode == EditAccount.RESULT_OK) {
                    model.getDocumentSnapShot(new TeacherContract.Model.Listener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot list) {
                            view.updateAcademics(list);
                            String profileComPercent = list.getString("pro_com_%");
                            String generatePercent = view.generatePercent();
                            if (!generatePercent.equals(profileComPercent)) {
                                model.updatePercentage(generatePercent);
                            }
                            if (profileComPercent != null) {
                                if (profileComPercent.equals("100")) {
                                    view.initProfileCompleteView();
                                    view.setProfileComPercent(profileComPercent);
                                } else {
                                    view.setProfileComPercent(profileComPercent);
                                }
                            }
                            view.generatePercent();
                            view.disableLoad();
                        }

                        @Override
                        public void onError(String msg) {
                            view.toast(msg);
                        }
                    });
                }
                break;
        }


    }

    public void uploadImage() {
        model.uploadImage(imageUri, new EditContract.DownloadListener() {
            @Override
            public void onSuccess(String url) {
                view.setAvatarUrl(url);
                view.disableLoad();
            }

            @Override
            public void onFail(String msg) {
                view.toast(msg);
                view.disableLoad();
            }
        });
    }

    @Override
    public void onUpdateSuccess() {
        hideKeyboard(activity);
        view.disableLoad();
        view.snakbar("Profile Updated Successfully");
        view.someThingChanged(false);
    }

    @Override
    public void onFail(String error) {
        view.toast(error);
    }
}
