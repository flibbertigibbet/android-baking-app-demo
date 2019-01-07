package com.banderkat.recipes.activities;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.RemoteViews;

import com.banderkat.recipes.IngredientsContentProvider;
import com.banderkat.recipes.R;
import com.banderkat.recipes.services.IngredientsWidgetService;

class IngredientsContentProviderObserver extends ContentObserver {
    private AppWidgetManager mAppWidgetManager;
    private ComponentName mComponentName;

    private static final String LOG_LABEL = "ContentObserver";

    IngredientsContentProviderObserver(AppWidgetManager mgr, ComponentName cn, Handler h) {
        super(h);
        mAppWidgetManager = mgr;
        mComponentName = cn;
    }

    @Override
    public void onChange(boolean selfChange) {
        // The data has changed, so notify the widget that the collection view needs to be updated.
        // In response, the factory's onDataSetChanged() will be called which will requery the
        // cursor for the new data.

        Log.d(LOG_LABEL, "ingredients provider content changed");
        mAppWidgetManager.notifyAppWidgetViewDataChanged(
                mAppWidgetManager.getAppWidgetIds(mComponentName), R.id.stack_view);
    }
}


/**
 * Implementation of App Widget functionality.
 */
public class IngredientsAppWidget extends AppWidgetProvider {

    private static final String LOG_LABEL = "Widget";

    private static IngredientsContentProviderObserver observer;
    private static HandlerThread workerThread;
    private static Handler workerQueue;

    private static int someCounter = 0;

    public IngredientsAppWidget() {
        workerThread = new HandlerThread("ingredients-widget-worker");
        workerThread.start();
        workerQueue = new Handler(workerThread.getLooper());
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_app_widget);

        final Intent intent = new Intent(context, IngredientsWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra("COUNTER", someCounter);
        someCounter++;
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        views.setRemoteAdapter(R.id.stack_view, intent);

        views.setEmptyView(R.id.stack_view, R.id.empty_view);

        views.setTextViewText(R.id.widget_item, widgetText);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.d(LOG_LABEL, "update app widget");
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d(LOG_LABEL, "widget enabled");
        // Register for external updates to the data to trigger an update of the widget.  When using
        // content providers, the data is often updated via a background service, or in response to
        // user interaction in the main app.  To ensure that the widget always reflects the current
        // state of the data, we must listen for changes and update ourselves accordingly.
        final ContentResolver r = context.getContentResolver();
        if (observer == null) {
            Log.d(LOG_LABEL, "set up widget provider observer");
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, IngredientsAppWidget.class);
            observer = new IngredientsContentProviderObserver(mgr, cn, workerQueue);
            r.registerContentObserver(IngredientsContentProvider.CONTENT_URI, true, observer);
        }
    }

    @Override
    public void onDisabled(Context ctx) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(LOG_LABEL, "widget disabled");
    }

    @Override
    public void onReceive(Context ctx, Intent intent) {
        final String action = intent.getAction();
        Log.d(LOG_LABEL, "widget onRecieve with action " + action);
        super.onReceive(ctx, intent);
    }
}

