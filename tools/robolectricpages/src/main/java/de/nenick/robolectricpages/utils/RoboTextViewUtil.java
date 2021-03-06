package de.nenick.robolectricpages.utils;

import android.view.View;
import android.widget.TextView;

public class RoboTextViewUtil {

    public static String getTextFromView(View rootView, int resourceId) {
        View view = rootView.findViewById(resourceId);
        if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        }
        throw new IllegalStateException(view.getClass() + " is not handled yet.");
    }
}
