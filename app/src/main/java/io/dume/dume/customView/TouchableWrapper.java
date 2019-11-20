package io.dume.dume.customView;
import android.content.Context;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import io.dume.dume.student.pojo.BaseMapActivity;

public class TouchableWrapper extends FrameLayout {

    private UpdateMapAfterUserInterection updateMapAfterUserInterection;
    private long lastTouched = 0;
    private static final long SCROLL_TIME = 200L;

    public TouchableWrapper(Context context) {
        super(context);
        // Force the host activity to implement the UpdateMapAfterUserInterection Interface
        try {
            updateMapAfterUserInterection = (BaseMapActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement UpdateMapAfterUserInterection");
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouched = SystemClock.uptimeMillis();
                updateMapAfterUserInterection.onMapTouchStart();
                break;
            case MotionEvent.ACTION_UP:
                final long now = SystemClock.uptimeMillis();
                if (now - lastTouched > SCROLL_TIME) {
                    // Update the map
                    updateMapAfterUserInterection.onUpdateMapAfterUserInterection();
                }
                updateMapAfterUserInterection.onMapTouchEnd();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    // Map Activity must implement this interface
    public interface UpdateMapAfterUserInterection {
        void onUpdateMapAfterUserInterection();
        void onMapTouchStart();
        void onMapTouchEnd();
    }
}