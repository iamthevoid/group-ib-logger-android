package iam.thevoid.logger;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

final class Util {

    private Util() {}

    static String getViewHierarchyDump(Activity activity) {
        return getViewHierarchyDump(activity.getWindow().getDecorView());
    }

    static String getViewHierarchyDump(Fragment fragment) {
        return getViewHierarchyDump(fragment.getView());
    }

    private static String getViewHierarchyDump(View view) {
        return getViewHierarchyDump(view, 0);
    }

    private static String getViewHierarchyDump(View view, int depth) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            builder.append("---");
        }
        builder.append(" ").append(getViewInfo(view)).append("\n");
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0, l = vg.getChildCount(); i < l; i++) {
                builder.append(getViewHierarchyDump(vg.getChildAt(i), depth + 1));
            }
        }
        return builder.toString();
    }

    public static String getViewInfo(View view) {
        return view == null ? "NULL" : view.toString();
    }

    public static String getMotionEventInfo(MotionEvent event) {
        return event == null ? "UNKNOWN" : event.toString();
    }
}
