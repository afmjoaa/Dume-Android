package io.dume.dume.library.contract;


/**
 * Created by amal.chandran on 15/11/17.
 */

public interface Animator {
    void play();

    void stop(AnimationCallback callback);
}
