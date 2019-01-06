package com.banderkat.recipes.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;
import android.database.Cursor;

import com.banderkat.recipes.data.models.Ingredient;
import com.banderkat.recipes.data.models.Recipe;

import java.util.List;

/**
 * Database access methods for destinations.
 */

@Dao
public abstract class RecipeDao {

    private static final String LOG_LABEL = "RecipeDao";

    @Transaction
    @Update()
    public abstract void update(Recipe obj);

    @Transaction
    @Delete()
    public abstract void delete(Recipe obj);

    @Transaction
    @Update()
    public abstract void bulkUpdate(List<Recipe> objs);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void save(Recipe obj);

    @Transaction
    @Query("SELECT * FROM recipe")
    public abstract LiveData<List<Recipe>> getAll();

    @Transaction
    @Query("SELECT * " +
            "FROM recipe " +
            "WHERE recipe.id = :recipeId ")
    public abstract LiveData<Recipe> getRecipe(long recipeId);

    @Query("SELECT * " +
            "FROM ingredient " +
            "WHERE recipeId = :recipeId ")
    public abstract Cursor getIngredients(long recipeId);

    @Transaction
    @Query("DELETE FROM recipe")
    public abstract void clear();
}
