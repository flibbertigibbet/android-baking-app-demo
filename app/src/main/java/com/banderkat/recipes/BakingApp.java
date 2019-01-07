package com.banderkat.recipes;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ContentProvider;
import android.util.Log;

import com.banderkat.recipes.di.AppInjector;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasContentProviderInjector;
import dagger.android.HasServiceInjector;

public class BakingApp extends Application implements HasActivityInjector, HasContentProviderInjector, HasServiceInjector {

    private static final String LOG_LABEL = "BakingApp";

    @SuppressWarnings("WeakerAccess")
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @SuppressWarnings("WeakerAccess")
    @Inject
    DispatchingAndroidInjector<Service> dispatchingServiceInjector;

    @SuppressWarnings("WeakerAccess")
    @Inject
    DispatchingAndroidInjector<ContentProvider> dispatchingContentProviderInjector;

    private volatile boolean needToInject = true;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Log.d(LOG_LABEL, "Running in debug mode");
        }
        // Lazy initialization to support injection for content provider. See:
        // https://github.com/google/dagger/blob/master/java/dagger/android/DaggerApplication.java
        injectIfNecessary();
    }

    /**
     * Lazily injects the {@link DaggerApplication}'s members. Injection cannot be performed in {@link
     * Application#onCreate()} since {@link android.content.ContentProvider}s' {@link
     * android.content.ContentProvider#onCreate() onCreate()} method will be called first and might
     * need injected members on the application. Injection is not performed in the constructor, as
     * that may result in members-injection methods being called before the constructor has completed,
     * allowing for a partially-constructed instance to escape.
     */
    private void injectIfNecessary() {
        if (needToInject) {
            synchronized (this) {
                if (needToInject) {
                    AppInjector.init(this);
                    if (needToInject) {
                        throw new IllegalStateException(
                                "The AndroidInjector returned from applicationInjector() did not inject the "
                                        + "DaggerApplication");
                    }
                }
            }
        }
    }

    @Inject
    void setInjected() {
        needToInject = false;
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingServiceInjector;
    }

    @Override
    public AndroidInjector<ContentProvider> contentProviderInjector() {
        injectIfNecessary();
        return dispatchingContentProviderInjector;
    }
}
