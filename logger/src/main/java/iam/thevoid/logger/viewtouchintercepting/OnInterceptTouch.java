package iam.thevoid.logger.viewtouchintercepting;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public interface OnInterceptTouch {
    void onInterceptTouch(@Nullable View view, MotionEvent event);
}
