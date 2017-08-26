package org.accapto.accaptoaccessibilityexample.accessibilityhelper;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krajn on 26/08/17.
 */

public class TouchAreaExtender {

   // public static void enhanceTouchArea(final View v, final int extraPadding) {

    public void enhanceTouchArea(final View v, final int extraPadding) {

        final View parent = (View) v.getParent();

        parent.post(new Runnable() {
            @Override
            public void run() {

                Rect delegateArea = new Rect();
                View delegate = v;

                delegate.getHitRect(delegateArea);

                delegateArea.top -= extraPadding;
                delegateArea.bottom += extraPadding;
                delegateArea.left -= extraPadding;
                delegateArea.right += extraPadding;

                TouchDelegateComposite tdc = new TouchDelegateComposite(delegateArea, delegate);
                tdc.addDelegate(new TouchDelegate(delegateArea, delegate));

                parent.setTouchDelegate(tdc);

            }
        });


    }


    /**
     * Helper class to enhance the touch area
     * <p>
     * see also  http://stackoverflow.com/questions/6799066/how-to-use-multiple-touchdelegate
     */

    private class TouchDelegateComposite extends TouchDelegate {

        private final List<TouchDelegate> delegates = new ArrayList<TouchDelegate>();

        public TouchDelegateComposite(Rect rectArea, View view) {
            super(rectArea, view);
        }

        public void addDelegate(TouchDelegate delegate) {
            if (delegate != null) {
                delegates.add(delegate);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            boolean res = false;
            float x = event.getX();
            float y = event.getY();
            for (TouchDelegate delegate : delegates) {
                event.setLocation(x, y);
                res = delegate.onTouchEvent(event) || res;
            }
            return res;
        }
    }
}
