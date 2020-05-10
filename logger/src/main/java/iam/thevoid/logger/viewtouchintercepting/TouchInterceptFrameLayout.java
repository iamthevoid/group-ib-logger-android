package iam.thevoid.logger.viewtouchintercepting;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class TouchInterceptFrameLayout extends FrameLayout {

    private OnInterceptTouch interceptor;

    private WeakReference<View> view;

    public TouchInterceptFrameLayout(@NonNull Context context) {
        super(context);
    }

    public void setInterceptor(OnInterceptTouch interceptor) {
        this.interceptor = interceptor;
    }

    public void setView(View view) {
        this.view = new WeakReference<>(view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View view = this.view.get();
        if (interceptor != null) {
            interceptor.onInterceptTouch(view, event);
        }
        return super.onTouchEvent(event);
    }

}
