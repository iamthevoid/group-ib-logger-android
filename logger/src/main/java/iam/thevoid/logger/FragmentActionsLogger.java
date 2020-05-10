package iam.thevoid.logger;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import iam.thevoid.logger.viewtouchintercepting.OnInterceptTouch;
import iam.thevoid.logger.viewtouchintercepting.TouchInterceptFrameLayout;
import iam.thevoid.logger.viewtouchintercepting.InterceptorTools;

class FragmentActionsLogger extends FragmentManager.FragmentLifecycleCallbacks
        implements OnInterceptTouch {

    private FragmentActionsLogger(){}

    private static volatile FragmentActionsLogger instance;

    static FragmentActionsLogger getInstance() {
        FragmentActionsLogger localInstance = instance;
        if (localInstance == null) {
            synchronized (FragmentActionsLogger.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new FragmentActionsLogger();
                }
            }
        }
        return localInstance;
    }

    @Override
    public void onFragmentPreAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
        super.onFragmentPreAttached(fm, f, context);
        onLifecycleEvent("onFragmentPreAttached", f);
    }

    @Override
    public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
        super.onFragmentAttached(fm, f, context);
        onLifecycleEvent("onFragmentAttached", f);
    }

    @Override
    public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
        super.onFragmentPreCreated(fm, f, savedInstanceState);
        onLifecycleEvent("onFragmentPreCreated", f);
    }

    @Override
    public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
        super.onFragmentCreated(fm, f, savedInstanceState);
        onLifecycleEvent("onFragmentCreated", f);
    }

    @Override
    public void onFragmentActivityCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
        super.onFragmentActivityCreated(fm, f, savedInstanceState);
        onLifecycleEvent("onFragmentActivityCreated", f);
    }

    @Override
    public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onFragmentViewCreated(fm, f, v, savedInstanceState);
        onLifecycleEvent("onFragmentViewCreated", f);
        Logger.log(f.getClass().getSimpleName(), Util.getViewHierarchyDump(f));
    }

    @Override
    public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentStarted(fm, f);
        onLifecycleEvent("onFragmentStarted", f);
    }

    @Override
    public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentResumed(fm, f);
        onLifecycleEvent("onFragmentResumed", f);
    }

    @Override
    public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentPaused(fm, f);
        onLifecycleEvent("onFragmentPaused", f);
    }

    @Override
    public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentStopped(fm, f);
        onLifecycleEvent("onFragmentStopped", f);
    }

    @Override
    public void onFragmentSaveInstanceState(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Bundle outState) {
        super.onFragmentSaveInstanceState(fm, f, outState);
        onLifecycleEvent("onFragmentSaveInstanceState", f);
    }

    @Override
    public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentViewDestroyed(fm, f);
        onLifecycleEvent("onFragmentViewDestroyed", f);
    }

    @Override
    public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentDestroyed(fm, f);
        onLifecycleEvent("onFragmentDestroyed", f);
    }

    @Override
    public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
        super.onFragmentDetached(fm, f);
        onLifecycleEvent("onFragmentDetached", f);
    }

    @Override
    public void onInterceptTouch(@Nullable View view, MotionEvent event) {
        Logger.log("Fragment#onInterceptTouch", "View = " + Util.getViewInfo(view));
        Logger.log("Fragment#onInterceptTouch", "MotionEvent = " + Util.getMotionEventInfo(event));
    }

    private void onLifecycleEvent(String message, Fragment fragment) {
        Logger.log(fragment.getClass().getSimpleName(), message);
        View view = fragment.getView();
        InterceptorTools.setViewTreeTouchDelegate(view, this);
    }
}
