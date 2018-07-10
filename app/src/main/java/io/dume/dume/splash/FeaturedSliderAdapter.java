package io.dume.dume.splash;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import java.util.Random;

import io.dume.dume.R;

public class FeaturedSliderAdapter extends BaseSliderView {
    private Random randomNumber;
    private static final String TAG = "FeaturedSliderAdapter";

    public FeaturedSliderAdapter(Context context) {
        super(context);
        randomNumber = new Random();

    }


    int i;

    @Override
    public View getView() {
        int i = randomNumber.nextInt(5) + 1;

        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_text, null);
        RelativeLayout slideContainer = v.findViewById(R.id.slideContainer);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

            switch (i) {
                case 1:
                    slideContainer.setBackground(getContext().getResources().getDrawable(R.drawable.slider_background_1));
                    break;
                case 2:
                    slideContainer.setBackground(getContext().getResources().getDrawable(R.drawable.slider_background_2));
                    break;
                case 3:
                    slideContainer.setBackground(getContext().getResources().getDrawable(R.drawable.slider_background_3));

                    break;
                case 4:
                    slideContainer.setBackground(getContext().getResources().getDrawable(R.drawable.slider_background_4));

                    break;
                case 5:
                    slideContainer.setBackground(getContext().getResources().getDrawable(R.drawable.slider_background_5));

                    break;
            }
        }
        ImageView target = (ImageView) v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView) v.findViewById(R.id.description);
        description.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Paprika-Regular.ttf"));
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}