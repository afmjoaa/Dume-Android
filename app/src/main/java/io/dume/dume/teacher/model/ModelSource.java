package io.dume.dume.teacher.model;


import java.util.Random;

import io.dume.dume.teacher.homepage.MainContract;

public class ModelSource implements MainContract.Model {
    CallBack listener;

    /**
     * set a callback listener before calling model data its nullable
     *
     * @param listener
     */
    @Override
    public void setListener(CallBack listener) {

    }

    /**
     * it returns a random number bound of 5000
     *
     * @return int
     */
    @Override
    public int getRandomNumber() {
        return new Random().nextInt(500);
    }

}
