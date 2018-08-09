package com.banderkat.recipes.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.banderkat.recipes.data.models.Recipe;

import java.util.List;

/**
 * Database access methods for destinations.
 */

@Dao
public abstract class RecipeDao {

    private static final String LOG_LABEL = "RecipeDao";

    @Update()
    public abstract void update(Recipe obj);

    @Delete()
    public abstract void delete(Recipe obj);

    @Update()
    public abstract void bulkUpdate(List<Recipe> objs);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void save(Recipe obj);

    @Transaction
    @Query("SELECT * FROM recipe")
    public abstract LiveData<List<Recipe>> getAll();

    @Query("SELECT * " +
            "FROM recipe " +
            "WHERE recipe.id = :recipeId ")
    public abstract LiveData<Recipe> getRecipe(long recipeId);

    @Query("DELETE FROM recipe")
    public abstract void clear();
}
