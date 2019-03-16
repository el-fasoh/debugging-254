package com.fasoh.debugginglikeaboss;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class DebuggingApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if(BuildConfig.BUILD_TYPE.equalsIgnoreCase("debug"))
        Timber.plant(new Timber.DebugTree());
        else
            Timber.plant(new ReleaseTree());

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
