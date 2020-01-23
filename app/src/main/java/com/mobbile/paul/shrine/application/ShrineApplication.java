package com.mobbile.paul.shrine.application;

import android.app.Application;
import android.content.Context;
import androidx.appcompat.app.AppCompatDelegate;

import co.paystack.android.PaystackSdk;
import io.kommunicate.Kommunicate;

public class ShrineApplication extends Application {
    private static ShrineApplication instance;
    private static Context appContext;

    public static ShrineApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        this.setAppContext(getApplicationContext());
        Kommunicate.init(this, "361032c6e4fbbcd05310bf3e41ef4a489");

        PaystackSdk.initialize(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}