package com.banderkat.recipes.data;

import android.arch.lifecycle.ViewModel;

import javax.inject.Inject;

public class RecipeViewModel extends ViewModel {

    private static final String LOG_LABEL = "ViewModel";

    public final RecipeRepository recipeRepository;

    @Inject
    public RecipeViewModel(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

}
