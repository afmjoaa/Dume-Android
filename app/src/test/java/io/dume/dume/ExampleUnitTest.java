package io.dume.dume;

import android.util.Log;

import org.junit.Test;

import io.dume.dume.homepage.MainContract;
import io.dume.dume.homepage.MainPresenter;
import io.dume.dume.model.ModelSource;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void isMultiplyCorrect() {
        assertEquals(5, multiply(3, 2));
    }

    public int multiply(int x, int y)

    {
        return x * y;
    }


    interface MockContract extends MainContract {

    }

    class MockPresenter extends MainPresenter {
        public MockPresenter(MainContract.View view, ModelSource model) {
            super(view, model);
        }
    }


}