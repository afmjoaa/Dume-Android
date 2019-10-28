package io.dume.dume.FirstTimeUser.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import carbon.widget.ImageView;
import io.dume.dume.R;
import io.dume.dume.FirstTimeUser.AfterSplashActivity;

public class DemoCardFragment extends Fragment
        implements View.OnClickListener {

    private static final String ARG_ID = "id";
    private static final String ARG_TITLE = "title";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_IMAGERESOURCE = "imageId";
    private static final String TAG = "DemoCardFragment";

    private int id, imageSrc;
    private String title, description;
    private OnActionListener actionListener;
    private Context context;
    private AfterSplashActivity myMainActivity;

    public DemoCardFragment() {

    }

    public static DemoCardFragment newInstance(int id, String title, String description, int imageSrc) {
        DemoCardFragment fragment = new DemoCardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_DESCRIPTION, description);
        args.putInt(ARG_IMAGERESOURCE, imageSrc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.id = getArguments().getInt(ARG_ID);
            this.title = getArguments().getString(ARG_TITLE);
            this.description = getArguments().getString(ARG_DESCRIPTION);
            this.imageSrc = getArguments().getInt(ARG_IMAGERESOURCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(id ==0){
            View v = inflater.inflate(R.layout.select_role, container, false);
            /*TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            TextView tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            tvTitle.setText(title);
            tvDescription.setText(description);*/
            return v;
        }else{
            View v = inflater.inflate(R.layout.trp_fragment_after_splash_cardview, container, false);
            TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            TextView tvDescription = (TextView) v.findViewById(R.id.tvDescription);
            ImageView afterSplashImageView = v.findViewById(R.id.after_splash_imageview);
            tvTitle.setText(title);
            tvDescription.setText(description);
            afterSplashImageView.setImageResource(imageSrc);
            return v;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            myMainActivity = (AfterSplashActivity) getActivity();
            if(myMainActivity!= null){
                switch (id){
                    case 0:
                        myMainActivity.afterSplashBtn.setVisibility(View.GONE);
                        break;
                    case 1:
                        myMainActivity.afterSplashBtn.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }else{

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                onAction();
                break;
        }
    }

    public void onAction() {
        if (actionListener != null) {
            actionListener.onAction(this.id);
        }
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        if (context instanceof OnActionListener) {
            actionListener = (OnActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        actionListener = null;
    }

    public interface OnActionListener {
        void onAction(int id);
    }
}