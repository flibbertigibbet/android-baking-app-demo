package com.banderkat.recipes.data.networkresource;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.banderkat.recipes.BuildConfig;
import com.banderkat.recipes.data.RecipeDao;
import com.banderkat.recipes.data.RecipeWebservice;
import com.banderkat.recipes.data.models.Ingredient;
import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.data.models.Step;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Network query manager.
 */

abstract public class RecipeNetworkBoundResource extends NetworkBoundResource<List<Recipe>, Recipe[]> {

    private static final String LOG_LABEL = "RecipeNetworkResource";

    // maximum rate at which to refresh data from network
    private static final long RATE_LIMIT = BuildConfig.DEBUG ? TimeUnit.MINUTES.toMillis(15):
            TimeUnit.HOURS.toMillis(12);

    private final RecipeWebservice webservice;
    private final RecipeDao recipeDao;

    public RecipeNetworkBoundResource(RecipeWebservice webservice, RecipeDao recipeDao) {
        this.webservice = webservice;
        this.recipeDao = recipeDao;
    }

    @Override
    protected void saveCallResult(@NonNull Recipe[] recipes) {
        Long timestamp = System.currentTimeMillis();

        // clear out existing database entries before adding new ones
        recipeDao.clear();

        // save recipes
        for (Recipe item : recipes) {
            item.setTimestamp(timestamp);

            // Explicitly set the related recipe ID on each of its ingredients and steps
            long id = item.getId();
            for (Ingredient ingredient : item.getIngredients()) {
                ingredient.setRecipeId(id);
            }
            for (Step step : item.getSteps()) {
                step.setRecipeId(id);
            }
            recipeDao.save(item);
        }
    }

    @Override
    protected boolean shouldFetch(@Nullable List<Recipe> data) {
        if (data == null || data.isEmpty()) {
            return true;
        }

        Recipe first = data.get(0);
        return System.currentTimeMillis() - first.getTimestamp() > RATE_LIMIT;
    }

    @NonNull @Override
    protected LiveData<ApiResponse<Recipe[]>> createCall() {
        return webservice.getRecipes();
    }
}
