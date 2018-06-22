package io.dume.dume.model;


import java.util.Random;

import io.dume.dume.homepage.MainContract;

public class ModelSource implements MainContract.Model {
    CallBack listener;

    @Override
    public int getRandomNumber() {
        return new Random().nextInt(500);
    }

    @Override
    public void setListener(CallBack listener) {
        this.listener = listener;
    }
}
