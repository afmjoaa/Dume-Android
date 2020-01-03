package io.dume.dume.firstTimeUser;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.PictureResult;
import com.otaliastudios.cameraview.controls.Flash;
import com.otaliastudios.cameraview.controls.Hdr;

import java.util.List;

import androidx.annotation.NonNull;
import ch.halcyon.squareprogressbar.SquareProgressBar;
import io.dume.dume.R;
import io.dume.dume.auth.auth_final.AuthRegisterActivity;
import io.dume.dume.student.pojo.BaseAppCompatActivity;
import io.dume.dume.util.StateManager;

import static io.dume.dume.util.DumeUtils.configureAppbar;

public class NIDScanActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private static final String TAG = "scanActivity";
    private CameraView camera;
    private boolean flag;
    private SquareProgressBar squareProgressBar;
    private Button howNIDScanWorkBtn;
    private Button dontHaveNIDBtn;
    private Dialog dialog;
    private BottomSheetDialog skipNIDScanDialog;
    private View skipNIDScanView;
    private StateManager stateManager;
    private Long NIDNo;
    private String NIDName;
    private String NIDBirthDate;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_nidscan);
        setActivityContext(this, 7766);
        configureAppbar(this, "NID Verification", true);
        initView();
        flag = false;
        stateManager = StateManager.Companion.getInstance(this);
        squareProgressBar.setWidth(7);
        squareProgressBar.setProgress(0);
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        squareProgressBar.setColor("#0288d1");
        squareProgressBar.setRoundedCorners(true, px);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                camera.takePictureSnapshot();
                if (!flag) {
                    handler.postDelayed(this, 1500);
                }
            }
        }, 2000);

        camera.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull PictureResult result) {
                super.onPictureTaken(result);
                Bitmap bitmap = BitmapFactory.decodeByteArray(result.getData(), 0, result.getData().length, null);
                squareProgressBar.setImageBitmap(bitmap);
                FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
                if (firebaseVisionImage != null) {
                    recognizeText(firebaseVisionImage);
                } else {
                    Log.e("getText", "null image found");
                }
            }
        });
    }

    private void initView() {
        camera = findViewById(R.id.camera);
        squareProgressBar = findViewById(R.id.sprogressbar);
        howNIDScanWorkBtn = findViewById(R.id.howNIDScanWork);
        dontHaveNIDBtn = findViewById(R.id.dontHaveNIDBtn);
        howNIDScanWorkBtn.setOnClickListener(this);
        dontHaveNIDBtn.setOnClickListener(this);
        initDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nid_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.flush) {
            if(camera.getFlash() == Flash.TORCH){
                item.setIcon(getResources().getDrawable(R.drawable.flush_icon));
                camera.setFlash(Flash.OFF);
            }else{
                item.setIcon(getResources().getDrawable(R.drawable.no_flush_icon));
                camera.setFlash(Flash.TORCH);
            }
        }else if(id  == R.id.hdr){
            if(camera.getHdr() == Hdr.ON){
                item.setIcon(getResources().getDrawable(R.drawable.hdr_icon));
                camera.setHdr(Hdr.OFF);
            }else {
                item.setIcon(getResources().getDrawable(R.drawable.no_hdr_icon));
                camera.setHdr(Hdr.ON);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.howNIDScanWork) {
            dialog.show();
        } else if (id == R.id.dontHaveNIDBtn) {
            skipNIDScanDialog.show();
        }
    }

    public void initDialog() {
        //custom dialog
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered);
        View customLayout = getLayoutInflater().inflate(R.layout.custom_nid_scan_info, null, false);
        materialAlertDialogBuilder.setView(customLayout);
        dialog = materialAlertDialogBuilder.create();
        try {
            Button dismissBtn = customLayout.findViewById(R.id.dismiss_btn);
            dismissBtn.setOnClickListener(v -> dialog.dismiss());
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        skipNIDScanDialog = new BottomSheetDialog(this);
        skipNIDScanView = this.getLayoutInflater().inflate(R.layout.custom_bottom_sheet_dialogue_cancel, null);
        skipNIDScanDialog.setContentView(skipNIDScanView);

        TextView mainText = skipNIDScanDialog.findViewById(R.id.main_text);
        TextView subText = skipNIDScanDialog.findViewById(R.id.sub_text);
        Button cancelYesBtn = skipNIDScanDialog.findViewById(R.id.cancel_yes_btn);
        Button cancelNoBtn = skipNIDScanDialog.findViewById(R.id.cancel_no_btn);
        if (mainText != null && subText != null && cancelYesBtn != null && cancelNoBtn != null) {
            mainText.setText("Skip NID verification ?");
            cancelYesBtn.setText("Yes, Skip");
            cancelNoBtn.setText("Cancel");
            subText.setText("Please note until NID verification you won't get any tuition. If you skip now you can provide it from your profile after initial setup...");

            cancelNoBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    skipNIDScanDialog.dismiss();
                }
            });

            cancelYesBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    skipNIDScanDialog.dismiss();
                    //got to next activity called
                    flush("go to next activity");
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.destroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.open();
    }

    private void recognizeText(FirebaseVisionImage image) {

        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        Task<FirebaseVisionText> result =
                detector.processImage(image)
                        .addOnSuccessListener(firebaseVisionText -> {
                            //Log.e(TAG, firebaseVisionText.getText());
                            int validPercent = validDataPercent(firebaseVisionText);
                            squareProgressBar.setProgress(validPercent);
                            if(validPercent== 100){
                                //save to share preference
                                flag = true;
                                stateManager.setValue("NIDNo" , NIDNo);
                                stateManager.setValue("NIDName", NIDName );
                                stateManager.setValue("NIDBirthDate", NIDBirthDate );
                                //Log.e(TAG, "last: " + NIDNo + " " + NIDName + " " + NIDBirthDate );
                                //goto next activity
                                Intent intent = new Intent(getApplicationContext(), AuthRegisterActivity.class);
                                intent.putExtra("NIDNo",NIDNo );
                                intent.putExtra("NIDName",NIDName );
                                intent.putExtra("NIDBirthDate",NIDBirthDate );
                                startActivity(intent);


                            }

                        })
                        .addOnFailureListener(e -> Log.e("getText", "Failed to get text"));
    }

    private int validDataPercent(FirebaseVisionText firebaseVisionText){
        int returnPercent = 0;
        List<FirebaseVisionText.TextBlock> blocks = firebaseVisionText.getTextBlocks();
        for(int j = 0; j <blocks.size(); j++){
            FirebaseVisionText.TextBlock block = blocks.get(j);
            List<FirebaseVisionText.Line> lines = block.getLines();
            for(int i = 0; i < lines.size(); i++){
                FirebaseVisionText.Line line = lines.get(i);
                String inputLine = line.getText();
                if(inputLine.contains("National ID Card")){
                    returnPercent = returnPercent+25;
                }
                if(inputLine.contains("Birth")){
                    returnPercent = returnPercent+25;
                    NIDBirthDate = inputLine;
                }
                if(inputLine.contains("Name")){
                    try{
                        NIDName = blocks.get(j+1).getLines().get(0).getText();
                        //NIDName = lines.get(i+1).getText();
                        returnPercent = returnPercent+25;
                    }catch (Exception e){
                        Log.e(TAG, "error found !!!!" );
                    }
                }
                try{
                    NIDNo = Long.parseLong(inputLine.replaceAll("\\s+",""));
                    if (NIDNo >= 10) {
                        returnPercent = returnPercent+25;
                    }
                }catch (Exception e){
                    //Log.d(TAG, "catch" + e.getLocalizedMessage());
                }
            }
        }
        if(returnPercent > 100){
            return  100;
        }else {
            return returnPercent;
        }
    }

}
