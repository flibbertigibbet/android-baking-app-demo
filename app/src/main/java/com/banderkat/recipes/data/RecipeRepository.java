package com.banderkat.recipes.data;

import android.arch.lifecycle.LiveData;

import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.data.networkresource.RecipeNetworkBoundResource;
import com.banderkat.recipes.data.networkresource.Resource;

import java.util.List;

import javax.inject.Inject;

public class RecipeRepository {

    private static final String LOG_LABEL = "RecipeRepository";

    public final RecipeWebservice recipeWebservice;
    public final RecipeDao recipeDao;

    @Inject
    public RecipeRepository(RecipeWebservice recipeWebservice, RecipeDao recipeDao) {
        this.recipeWebservice = recipeWebservice;
        this.recipeDao = recipeDao;
    }

    public LiveData<Recipe> getRecipe(long RecipeId) {
        // return a LiveData item directly from the database.
        return recipeDao.getRecipe(RecipeId);
    }

    public LiveData<Resource<List<Recipe>>> loadRecipes() {
        return new RecipeNetworkBoundResource(recipeWebservice, recipeDao).getAsLiveData();
    }
}
