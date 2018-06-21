package io.dume.dume.model;


import java.util.Random;

import io.dume.dume.interfaces.Model;

public class ModelSource implements Model.StringData {
    Model.StringData.DataListener listener;

    @Override
    public int getRandomNumber() {
        return new Random().nextInt(500);
    }

    @Override
    public void setListener(DataListener listener) {
        this.listener = listener;
    }
}
