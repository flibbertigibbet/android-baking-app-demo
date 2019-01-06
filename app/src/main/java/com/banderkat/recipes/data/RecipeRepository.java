package com.banderkat.recipes.data;

import android.arch.lifecycle.LiveData;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.data.networkresource.RecipeNetworkBoundResource;
import com.banderkat.recipes.data.networkresource.Resource;

import java.util.List;

import javax.inject.Inject;

public class RecipeRepository {

    private static final String LOG_LABEL = "RecipeRepository";

    public RecipeWebservice recipeWebservice;
    public RecipeDao recipeDao;

    @Inject
    public RecipeRepository(RecipeWebservice recipeWebservice, RecipeDao recipeDao) {
        this.recipeWebservice = recipeWebservice;
        this.recipeDao = recipeDao;
    }

    public LiveData<Recipe> getRecipe(long recipeId) {
        // return a LiveData item directly from the database.
        return recipeDao.getRecipe(recipeId);
    }

    public LiveData<Resource<List<Recipe>>> loadRecipes() {
        return new RecipeNetworkBoundResource(recipeWebservice, recipeDao) {
            @Override
            @NonNull
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.getAll();
            }
        }.getAsLiveData();
    }

    public Cursor getIngredients(long recipeId) {
        return recipeDao.getIngredients(recipeId);
    }
}
