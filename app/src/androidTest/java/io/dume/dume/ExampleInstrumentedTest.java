package io.dume.dume;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    // private Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @Test
    public void useAppContext() {
        // String text = String.format(context.getString(R.string.sent_code_msg), "+880 1536100076");
        // Log.e("debug", "useAppContext: " + text);
        //  Assert.assertNotSame(text, context.getString(R.string.sent_code_msg));
    }
}