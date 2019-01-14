package com.banderkat.recipes.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.database.Cursor;
import android.util.Log;

import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.data.models.Step;
import com.banderkat.recipes.data.networkresource.Resource;

import java.util.List;

import javax.inject.Inject;

public class RecipeViewModel extends ViewModel {

    private static final String LOG_LABEL = "ViewModel";

    protected RecipeRepository recipeRepository;

    private LiveData<Resource<List<Recipe>>> recipes;

    @Inject
    public RecipeViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
        recipes = recipeRepository.loadRecipes();
    }

    public LiveData<Resource<List<Recipe>>> getRecipes() {
        return recipes;
    }

    public LiveData<Recipe> getRecipe(long id) {
        return recipeRepository.getRecipe(id);
    }

}
