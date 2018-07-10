package io.dume.dume.auth;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;


import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import io.dume.dume.R;
import io.dume.dume.splash.FeaturedSliderAdapter;


public class AuthActivity extends AppCompatActivity implements AuthContract.View {
    SliderLayout sliderLayout;
    AuthContract.Presenter presenter;
    private String[] stringArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_authentication);
        presenter = new AuthPresenter(this, null);
        presenter.enqueue();
    }


    @Override
    public void init() {
        stringArray = getResources().getStringArray(R.array.featured_text_array);
        sliderLayout.setCustomIndicator(findViewById(R.id.page_indicator));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(stringArray[0]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(stringArray[1]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(stringArray[2]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(stringArray[3]));
        sliderLayout.addSlider(new FeaturedSliderAdapter(this).image(R.drawable.slide_background).
                description(stringArray[4]));

    }

    @Override
    public void findView() {
        sliderLayout = findViewById(R.id.slidingLayout);
    }
}
