package io.dume.dume;

import android.util.Log;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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





}