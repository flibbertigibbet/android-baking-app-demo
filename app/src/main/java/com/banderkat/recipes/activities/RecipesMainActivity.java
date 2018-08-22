package com.banderkat.recipes.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import com.banderkat.recipes.fragments.RecipeListFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecipesMainActivity extends AppCompatActivity
        implements RecipeListAdapter.RecipeListItemClickListener, RecipeListFragment.OnFragmentInteractionListener {

    private static final String LOG_LABEL = "MainActivity";

    @Inject
    public RecipeViewModelFactory viewModelFactory;
    RecipeViewModel viewModel;

    private List<Recipe> recipeList;

    private RecyclerView recipeListRecyclerView;
    private RecipeListAdapter recipeListAdapter;
    private TextView noDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recipeListRecyclerView = findViewById(R.id.recipe_list_recycler_view);
        recipeListRecyclerView.setLayoutManager(layoutManager);

        noDataTextView = findViewById(R.id.recipe_list_no_data);
        recipeList = new ArrayList<>();

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
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(LOG_LABEL, "Fragment interaction callback");
    }
}
