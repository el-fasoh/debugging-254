package com.fasoh.debugginglikeaboss.network;

import android.content.Context;

import com.fasoh.debugginglikeaboss.BuildConfig;

public class BackendOneProvider extends ServiceBuilder {
    public static BackendOneService createService(Context context) {
        return createBaseService(BackendOneService.class, BuildConfig.SERVICE_ONE,context);
    }
}
