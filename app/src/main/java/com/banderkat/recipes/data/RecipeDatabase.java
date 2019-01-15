package com.banderkat.recipes.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.banderkat.recipes.data.models.Ingredient;
import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.data.models.Step;

@Database(version=4, entities={Recipe.class, Ingredient.class, Step.class})
public abstract class RecipeDatabase extends RoomDatabase {
    abstract public RecipeDao recipeDao();
}
