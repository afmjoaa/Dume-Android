package io.dume.dume.interfaces;

/*
joto gula activity thakbe totogula sub interface thakbe karon proti activity er jonno notun interface file create
 kora khub akta buddhimaner kaj na,so tai sub interface e korchi
 */
public interface Views {
    /* Interface for MainActivity.class */
    interface MainActivityView {
        void showRandomNumber(int i);
        void init();
    }

    /* Interface for WelcomeActivity.class */
    interface WelcomeActivityView {

    }

}
