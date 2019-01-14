package com.banderkat.recipes.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.banderkat.recipes.R;
import com.banderkat.recipes.data.RecipeViewModel;
import com.banderkat.recipes.di.RecipeViewModelFactory;
import com.banderkat.recipes.fragments.RecipeListFragment;
import com.banderkat.recipes.fragments.RecipeStepFragment;
import com.banderkat.recipes.fragments.StepDetailFragment;

import javax.inject.Inject;

public class RecipesMainActivity extends AppCompatActivity
        implements RecipeStepFragment.OnListFragmentInteractionListener {

    public static final String STEP_LIST_FRAGMENT = "step-list";
    public static final String STEP_DETAIL_FRAGMENT = "step-fragment";
    public static final String RECIPE_LIST_FRAGMENT = "recipe-list";

    private static final String LOG_LABEL = "MainActivity";
    private static final int RECIPE_CARD_WIDTH = 300;

    @Nullable
    private CountingIdlingResource countingIdlingResource;

    @Inject
    public RecipeViewModelFactory viewModelFactory;
    RecipeViewModel viewModel;

    /**
     * Share a single, lazily instantiated view model between the fragments managed by this activity.
     *
     * @return recipe view model
     */
    public RecipeViewModel getViewModel() {
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel.class);
        }
        return viewModel;
    }

    public RecipesMainActivity() {
        this.countingIdlingResource = new CountingIdlingResource("RecipesIdling");
    }

    public void incrementIdling() {
        countingIdlingResource.increment();
    }

    public void decrementIdling() {
        countingIdlingResource.decrement();
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
        setContentView(R.layout.activity_recipes);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                Log.d(LOG_LABEL, "have saved instance state");
                return;
            }

            incrementIdling();
            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeListFragment fragment = new RecipeListFragment();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.fragment_container, fragment, RECIPE_LIST_FRAGMENT);
            transaction.commit();
            fragmentManager.executePendingTransactions();
            decrementIdling();
        } else {
            Log.e(LOG_LABEL, "have no fragment container");
        }
    }

    public void goToStepList(long recipeId) {
        Log.d(LOG_LABEL, "go to step list for recipe ID: " + String.valueOf(recipeId));

        incrementIdling();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = RecipeStepFragment.newInstance(recipeId);
        transaction.replace(R.id.fragment_container, fragment, STEP_LIST_FRAGMENT);

        // Show first step by default in master-detail layout.
        if (findViewById(R.id.fragment_detail_container) != null) {
            Log.d(LOG_LABEL, "show details by step list");
            findViewById(R.id.fragment_detail_container).setVisibility(View.VISIBLE);
            Fragment stepFragment = StepDetailFragment.newInstance(recipeId, 0);
            transaction.add(R.id.fragment_detail_container, stepFragment, STEP_DETAIL_FRAGMENT);
        }
        transaction.addToBackStack(STEP_LIST_FRAGMENT);
        transaction.commit();
        fragmentManager.executePendingTransactions();
        decrementIdling();
    }

    public void goToRecipeStepDetail(long recipeId, int position) {
        Log.d(LOG_LABEL, "Loading detail for step at position " + position + " for recipe " + recipeId);
        incrementIdling();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = StepDetailFragment.newInstance(recipeId, position);

        // put in master-detail or to single fragment layout
        if (findViewById(R.id.fragment_detail_container) != null) {
            transaction.replace(R.id.fragment_detail_container, fragment, STEP_DETAIL_FRAGMENT);
        } else {
            transaction.replace(R.id.fragment_container, fragment, STEP_DETAIL_FRAGMENT);
            transaction.addToBackStack(STEP_DETAIL_FRAGMENT);
        }

        transaction.commit();
        fragmentManager.executePendingTransactions();
        decrementIdling();
    }

    @Override
    public void onListFragmentInteraction(long recipeId, int position) {
        Log.d(LOG_LABEL, "Clicked recipe step at position: " + position + " for recipe " + recipeId);
        goToRecipeStepDetail(recipeId, position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_LABEL, "picked menu item " + item);

        switch (item.getItemId()) {
            case android.R.id.home:
                onUpPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void onUpPressed() {
        Log.d(LOG_LABEL, "handle up press");

        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            Log.d(LOG_LABEL, "Navigate up to step list");
            fragmentManager.popBackStackImmediate(STEP_LIST_FRAGMENT, 0);
            fragmentManager.executePendingTransactions();
        } else {
            super.onBackPressed();
        }
    }

    @Nullable
    public CountingIdlingResource getCountingIdlingResource() {
        return countingIdlingResource;
    }
}
