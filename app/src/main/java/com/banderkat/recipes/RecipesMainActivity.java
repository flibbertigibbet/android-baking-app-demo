package com.banderkat.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.banderkat.recipes.data.RecipeViewModel;
import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.data.networkresource.Status;
import com.banderkat.recipes.di.RecipeViewModelFactory;

import javax.inject.Inject;

public class RecipesMainActivity extends AppCompatActivity {

    private static final String LOG_LABEL = "MainActivity";

    @Inject
    public RecipeViewModelFactory viewModelFactory;
    RecipeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel.class);
        viewModel.getRecipes().observe(this, recipeResource -> {
            if (recipeResource == null) {
                Log.e(LOG_LABEL, "No network resource found");
                return;
            }

            if (!recipeResource.status.equals(Status.SUCCESS)) {
                return; // likely still loading
            }

            if (recipeResource.data == null || recipeResource.data.isEmpty()) {
                Log.e(LOG_LABEL, "Results returned, but empty");
            }

            Log.d(LOG_LABEL, "Found recipes! Got: " + recipeResource.data.size());
            for (Recipe recipe: recipeResource.data) {
                Log.d(LOG_LABEL, recipe.getId());
            }
        });
    }
}
