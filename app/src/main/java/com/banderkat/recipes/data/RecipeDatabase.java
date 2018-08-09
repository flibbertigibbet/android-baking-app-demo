package com.banderkat.recipes.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.banderkat.recipes.data.models.Recipe;

@Database(version=1, entities={Recipe.class})
public abstract class RecipeDatabase extends RoomDatabase {
    abstract public RecipeDao recipeDao();
}
