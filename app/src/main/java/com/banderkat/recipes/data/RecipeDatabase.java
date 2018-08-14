package com.banderkat.recipes.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.banderkat.recipes.data.models.Ingredient;
import com.banderkat.recipes.data.models.Recipe;

@Database(version=2, entities={Recipe.class, Ingredient.class})
public abstract class RecipeDatabase extends RoomDatabase {
    abstract public RecipeDao recipeDao();
}
