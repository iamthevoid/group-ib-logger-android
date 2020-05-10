package iam.thevoid.logger.viewtouchintercepting;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;

import iam.thevoid.logger.R;

public final class InterceptorTools {
    private InterceptorTools(){}

    public static void setViewTreeTouchDelegate(View view, final OnInterceptTouch interceptor) {
        if (view == null) return;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0, l = viewGroup.getChildCount(); i < l; i++) {
                setViewTreeTouchDelegate(viewGroup.getChildAt(i), interceptor);
            }
        }
        setTouchDelegate(view, interceptor);
    }

    private static void setTouchDelegate(@NonNull final View receiver, final OnInterceptTouch interceptor) {
        Object tag = receiver.getTag(R.id.touch_interceptor);
        if (!(tag instanceof TouchDelegate)) {
            receiver.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    TouchDelegate delegate = new TouchDelegate(
                            viewRect(receiver),
                            createTouchInterceptorView(receiver, interceptor)
                    );
                    receiver.setTouchDelegate(delegate);
                    receiver.setTag(delegate);
                    receiver.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    receiver.addOnAttachStateChangeListener(new OnAttachStateChangeListenerAdapter() {
                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            v.setTag(R.id.touch_interceptor, null);
                        }
                    });
                }
            });
        }
    }

    private static View createTouchInterceptorView(
            @NonNull final View receiver,
            final OnInterceptTouch interceptor
    ) {
        TouchInterceptFrameLayout layout = new TouchInterceptFrameLayout(receiver.getContext());
        layout.setView(receiver);
        layout.setInterceptor(interceptor);
        return layout;
    }

    private static Rect viewRect(@NonNull final View receiver) {
        Rect rect = new Rect();
        receiver.getDrawingRect(rect);
        return rect;
    }
}
