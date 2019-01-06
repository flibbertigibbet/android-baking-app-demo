package com.banderkat.recipes;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.util.Log;

import com.banderkat.recipes.di.AppInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;

public class BakingApp extends Application implements HasActivityInjector, HasServiceInjector {

    private static final String LOG_LABEL = "BakingApp";

    @SuppressWarnings("WeakerAccess")
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @SuppressWarnings("WeakerAccess")
    @Inject
    DispatchingAndroidInjector<Service> dispatchingServiceInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Log.d(LOG_LABEL, "Running in debug mode");
        }
        AppInjector.init(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }
}
