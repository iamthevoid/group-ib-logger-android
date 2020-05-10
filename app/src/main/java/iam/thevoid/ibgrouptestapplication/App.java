package iam.thevoid.ibgrouptestapplication;

import android.app.Application;

import iam.thevoid.logger.Logger;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init(this);
    }
}
