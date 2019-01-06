package com.banderkat.recipes.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViewsService;

import dagger.android.AndroidInjection;

public class IngredientsWidgetService extends RemoteViewsService {

    private static final String LOG_LABEL = "WidgetService";

    public IngredientsWidgetService() {
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        Log.d(LOG_LABEL, "ingredients widget service onCreate");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return null;
    }
}
