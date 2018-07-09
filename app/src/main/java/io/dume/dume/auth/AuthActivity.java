package io.dume.dume.auth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;


import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import io.dume.dume.R;


public class AuthActivity extends AppCompatActivity implements AuthContract.View {
    SliderLayout sliderLayout;
    AuthContract.Presenter presenter;


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
        sliderLayout.setCustomIndicator(findViewById(R.id.page_indicator));
        sliderLayout.addSlider(new DefaultSliderView(this).image("https://images.pexels.com/photos/731082/pexels-photo-731082.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=550&w=300"));
        sliderLayout.addSlider(new DefaultSliderView(this).image("https://i.pinimg.com/originals/25/03/75/250375dc394e38a32bdd8b7ea485c44f.jpg"));
        sliderLayout.addSlider(new DefaultSliderView(this).image("https://images.pexels.com/photos/139935/pexels-photo-139935.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));
        sliderLayout.addSlider(new DefaultSliderView(this).image("https://images.pexels.com/photos/1210494/pexels-photo-1210494.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940"));
    }

    @Override
    public void findView() {
        sliderLayout = findViewById(R.id.slidingLayout);
    }
}
