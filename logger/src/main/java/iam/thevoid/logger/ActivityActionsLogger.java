package iam.thevoid.logger;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import iam.thevoid.logger.viewtouchintercepting.OnInterceptTouch;
import iam.thevoid.logger.viewtouchintercepting.TouchInterceptFrameLayout;
import iam.thevoid.logger.viewtouchintercepting.InterceptorTools;

class ActivityActionsLogger implements
        Application.ActivityLifecycleCallbacks,
        OnInterceptTouch {

    private ActivityActionsLogger(){}

    private static volatile ActivityActionsLogger instance;

    static ActivityActionsLogger getInstance() {
        ActivityActionsLogger localInstance = instance;
        if (localInstance == null) {
            synchronized (ActivityActionsLogger.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ActivityActionsLogger();
                }
            }
        }
        return localInstance;
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        onLifecycleEvent("onActivityCreated", activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        onLifecycleEvent("onActivityStarted", activity);
        Logger.log(activity.getClass().getSimpleName(), Util.getViewHierarchyDump(activity));
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        onLifecycleEvent("onActivityResumed", activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        onLifecycleEvent("onActivityPaused", activity);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        onLifecycleEvent("onActivityStopped", activity);
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        onLifecycleEvent("onActivitySaveInstanceState", activity);
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        onLifecycleEvent("onActivityDestroyed", activity);
    }

    @Override
    public void onInterceptTouch(@Nullable View view, MotionEvent event) {
        Logger.log("Activity#onInterceptTouch", "View = " + Util.getViewInfo(view));
        Logger.log("Activity#onInterceptTouch", "MotionEvent = " + Util.getMotionEventInfo(event));
    }

    private void onLifecycleEvent(String message, Activity activity) {
        Logger.log(activity.getClass().getSimpleName(), message);
        Window window = activity.getWindow();
        View view = window.peekDecorView();
        InterceptorTools.setViewTreeTouchDelegate(view, this);
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(FragmentActionsLogger.getInstance(), true);
        }
    }
}
