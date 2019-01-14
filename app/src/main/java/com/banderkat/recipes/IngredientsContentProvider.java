package com.banderkat.recipes;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.banderkat.recipes.data.RecipeDao;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class IngredientsContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.banderkat.recipes.provider");

    private static final String LOG_LABEL = "WidgetContentProvider";

    @SuppressWarnings("WeakerAccess")
    @Inject
    RecipeDao recipeDao;

    public IngredientsContentProvider() {
        Log.d(LOG_LABEL, "IngredientsContentProvider constructor");
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        Log.d(LOG_LABEL, "Ingredients content provider onCreate");
        AndroidInjection.inject(this);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Log.d(LOG_LABEL,"query content provider for recipes");

        if (recipeDao == null) {
            Log.e(LOG_LABEL, "Failed to find injected DAO for content provider");
            return null;
        }

        return recipeDao.getRecipeCursor();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
