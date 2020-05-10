package iam.thevoid.logger;

import androidx.annotation.NonNull;

public class ExceptionsHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = ExceptionsHandler.class.getSimpleName();

    private final Thread.UncaughtExceptionHandler delegate;

    ExceptionsHandler(Thread.UncaughtExceptionHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        delegate.uncaughtException(t, e);
        Logger.log(TAG, t.getName(), e);
    }
}
