package io.dume.dume.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import io.dume.dume.R;
import io.dume.dume.interfaces.Views;
import io.dume.dume.interfaces.Presenter;
import io.dume.dume.model.ModelSource;
import io.dume.dume.presenter.PresenterMainActivity;

public class MainActivity extends AppCompatActivity implements Views.MainActivityView {
    private Presenter.MainActivityPresenter presenter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new PresenterMainActivity(this, new ModelSource());
    }

    @Override
    public void showRandomNumber(int i) {
        textView.setText(String.valueOf(i));
    }

    @Override
    public void init() {
        textView = findViewById(R.id.my_textView);
    }


    public void toggle(View view) {
        presenter.onButtonClicked();

    }
}
