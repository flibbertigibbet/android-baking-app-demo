package com.banderkat.recipes.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.banderkat.recipes.R;
import com.banderkat.recipes.adapters.RecipeListAdapter;
import com.banderkat.recipes.data.RecipeViewModel;
import com.banderkat.recipes.data.models.Ingredient;
import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.data.models.Step;
import com.banderkat.recipes.data.networkresource.Status;
import com.banderkat.recipes.di.RecipeViewModelFactory;
import com.banderkat.recipes.fragments.RecipeStepFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipesMainActivity extends AppCompatActivity
        implements RecipeListAdapter.RecipeListItemClickListener, RecipeStepFragment.OnListFragmentInteractionListener {

    private static final String LOG_LABEL = "MainActivity";
    private static final int RECIPE_CARD_WIDTH = 300;

    @Inject
    public RecipeViewModelFactory viewModelFactory;
    RecipeViewModel viewModel;

    private List<Recipe> recipeList;

    private RecyclerView recipeListRecyclerView;
    private RecipeListAdapter recipeListAdapter;
    private TextView noDataTextView;

    /** Share a single, lazily instantiated view model between the fragments managed by this activity.
     *
     * @return recipe view model
     */
    public RecipeViewModel getViewModel() {
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel.class);
        }
        return viewModel;
    }

    public int getGridSpanCount() {
        int spanCount = 1;
        // Get display width, in DP, to determine number of card columns to display
        Configuration configuration = getResources().getConfiguration();
        int screenWidthDp = configuration.screenWidthDp;

        Log.d(LOG_LABEL, "Display width in DP: " + screenWidthDp);

        spanCount = screenWidthDp / RECIPE_CARD_WIDTH;
        if (spanCount == 0) {
            spanCount = 1;
        }
        return spanCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(LOG_LABEL, "main activity onCreate");

        setContentView(R.layout.activity_recipes);

        RecyclerView.LayoutManager layoutManager;

        int spanCount = getGridSpanCount();
        layoutManager = new GridLayoutManager(this, spanCount);

        recipeListRecyclerView = findViewById(R.id.recipe_list_recycler_view);
        recipeListRecyclerView.setLayoutManager(layoutManager);

        noDataTextView = findViewById(R.id.recipe_list_no_data);
        recipeList = new ArrayList<>();

        // Lazily initialize view model, if it's not already set
        getViewModel().getRecipes().observe(this, recipeResource -> {
            if (recipeResource == null) {
                Log.e(LOG_LABEL, "No network resource found");
                return;
            }

            if (!recipeResource.status.equals(Status.SUCCESS)) {
                return; // likely still loading
            }

            if (recipeResource.data == null || recipeResource.data.isEmpty()) {
                Log.e(LOG_LABEL, "Results returned, but empty");
                recipeList = new ArrayList<>();
            }

            Log.d(LOG_LABEL, "Found recipes! Got: " + recipeResource.data.size());
            for (Recipe recipe: recipeResource.data) {
                Log.d(LOG_LABEL, recipe.getId() + ": " + recipe.getName());
                for (Ingredient ingredient : recipe.getIngredients()) {
                    Log.d(LOG_LABEL, "  Ingredient for recipe #" + ingredient.getRecipeId() +
                            ": " + ingredient.getId() + " - " + ingredient.getIngredient());
                }

                for (Step step: recipe.getSteps()) {
                    Log.d(LOG_LABEL, "  Step for recipe #" + step.getRecipeId() +
                            ": " + step.getId() + " - " + step.getShortDescription());
                }
            }

            recipeList = recipeResource.data;
            loadData();
        });
    }

    private void loadData() {
        Log.d(LOG_LABEL, "load recipes into list");
        if (recipeListAdapter == null || recipeList.size() != recipeListAdapter.getItemCount()) {
            recipeListAdapter = new RecipeListAdapter(this, recipeList, this);
            recipeListAdapter.submitList(recipeList);
            recipeListRecyclerView.setAdapter(recipeListAdapter);
        } else {
            // submit list for diff
            recipeListAdapter.submitList(recipeList);
        }

        recipeListAdapter.notifyDataSetChanged();
        recipeListRecyclerView.requestLayout();

        if (recipeList.isEmpty()) {
            recipeListRecyclerView.setVisibility(View.GONE);
            noDataTextView.setVisibility(View.VISIBLE);
        } else {
            recipeListRecyclerView.setVisibility(View.VISIBLE);
            noDataTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void clickedRecipe(int position) {
        Log.d(LOG_LABEL, "Clicked recipe at position: " + position);

        Recipe recipe = recipeList.get(position);
        Log.d(LOG_LABEL, "Recipe name: " + recipe.getName());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment fragment = RecipeStepFragment.newInstance(recipe.getId());

        transaction.replace(R.id.recipe_activity_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onListFragmentInteraction(long recipeId, int position) {
        Log.d(LOG_LABEL, "Clicked recipe step at position: " + position + " for recipe " + recipeId);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d(LOG_LABEL, "onBackPressed");

        // TODO: check the currently showing fragment, to support back to recipe step
        // currently assume if back pressed, got back to list view
        setTitle(getString(R.string.app_name));
    }
}
