package io.dume.dume.homepage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import io.dume.dume.R;
import io.dume.dume.model.ModelSource;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    private MainContract.Presenter presenter;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter(this, new ModelSource());
    }

    @Override
    public void showRandomNumber(int i) {
        textView.setText(String.valueOf(i));
    }

    @Override
    public void init() {
        textView = findViewById(R.id.my_textView);
    }


    public void toggle(android.view.View view) {
        presenter.onButtonClicked();

    }
}
