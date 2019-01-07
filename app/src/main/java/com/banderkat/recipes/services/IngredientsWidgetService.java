package com.banderkat.recipes.services;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.banderkat.recipes.IngredientsContentProvider;
import com.banderkat.recipes.R;
import com.banderkat.recipes.data.IngredientConverter;
import com.banderkat.recipes.data.models.Ingredient;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import dagger.android.AndroidInjection;

public class IngredientsWidgetService extends RemoteViewsService {

    private static final String LOG_LABEL = "WidgetService";

    public IngredientsWidgetService() {
        Log.d(LOG_LABEL, "ingredients widget service constructor");
    }

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        Log.d(LOG_LABEL, "ingredients widget service onCreate");
        super.onCreate();
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d(LOG_LABEL, "onGetViewFactory");
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

/**
 * This is the factory that will provide data to the collection widget.
 */
class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Cursor mCursor;
    private IngredientConverter ingredientsConverter;
    NumberFormat numberFormat;

    private static final String LOG_LABEL = "StackRemoteViewsFactory";

    public StackRemoteViewsFactory(Context context, Intent intent) {
        Log.d(LOG_LABEL, "constructor");
        mContext = context;
    }

    public void onCreate() {
        Log.d(LOG_LABEL, "onCreate");
        ingredientsConverter = new IngredientConverter();
        numberFormat = NumberFormat.getNumberInstance(Locale.US);
    }

    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    public int getCount() {
        Log.d(LOG_LABEL, "Returning count " + mCursor.getCount());
        return mCursor.getCount();
    }

    public RemoteViews getViewAt(int position) {
        // Get the data for this position from the content provider

        Log.d(LOG_LABEL, "getViewAt " + position);

        String text = "FIXME";
        long recipeId = -1;
        if (mCursor.moveToPosition(position)) {
            final int ingredientIndex = mCursor.getColumnIndex("ingredients");
            final int recipeNameIndex = mCursor.getColumnIndex("name");
            String recipeName = mCursor.getString(recipeNameIndex);
            String ingredientsString = mCursor.getString(ingredientIndex);
            List<Ingredient> ingredientList = ingredientsConverter.toIngredientList(ingredientsString);

            text = recipeName + ":\n\n";
            for (Ingredient ingredient: ingredientList) {
                text += numberFormat.format(ingredient.getQuantity()) + " ";
                if (!ingredient.getMeasure().contains("UNIT")) {
                    text += ingredient.getMeasure() + " ";
                }
                text += ingredient.getIngredient() + "\n";
            }
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_app_widget_item);
        rv.setTextViewText(R.id.widget_item, text);

        return rv;
    }
    public RemoteViews getLoadingView() {
        Log.d(LOG_LABEL, "getLoadingView");
        return null;
    }

    public int getViewTypeCount() {
        return 2;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {

        Log.d(LOG_LABEL, "onDataSetChanged");
        // Refresh the cursor
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = mContext.getContentResolver().query(IngredientsContentProvider.CONTENT_URI,
                null, null, null, null);
    }
}

