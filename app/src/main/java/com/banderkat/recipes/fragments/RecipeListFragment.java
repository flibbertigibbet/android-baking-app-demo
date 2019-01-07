package com.banderkat.recipes.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.banderkat.recipes.R;
import com.banderkat.recipes.activities.RecipesMainActivity;
import com.banderkat.recipes.adapters.RecipeListAdapter;
import com.banderkat.recipes.data.models.Ingredient;
import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.data.models.Step;
import com.banderkat.recipes.data.networkresource.Status;

import java.util.ArrayList;
import java.util.List;


public class RecipeListFragment extends Fragment implements RecipeListAdapter.RecipeListItemClickListener {

    private static final String LOG_LABEL = "RecipeListFragment";

    private List<Recipe> recipeList;

    private RecipesMainActivity activity;
    private RecyclerView recipeListRecyclerView;
    private RecipeListAdapter recipeListAdapter;
    private TextView noDataTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(LOG_LABEL, "create recipe list view");
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        activity = (RecipesMainActivity)getActivity();
        activity.setIdleState(false);

        recipeListRecyclerView = view.findViewById(R.id.recipe_list_recycler_view);

        if (activity.findViewById(R.id.fragment_detail_container) != null) {
            activity.findViewById(R.id.fragment_detail_container).setVisibility(View.GONE);
        }

        RecyclerView.LayoutManager layoutManager;

        int spanCount = activity.getGridSpanCount();
        layoutManager = new GridLayoutManager(activity, spanCount);

        recipeListRecyclerView.setLayoutManager(layoutManager);

        noDataTextView = view.findViewById(R.id.recipe_list_no_data);
        recipeList = new ArrayList<>();

        // Lazily initialize view model, if it's not already set
        activity.getViewModel().getRecipes().observe(this, recipeResource -> {
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
            activity.setIdleState(true);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(LOG_LABEL, "onViewCreated");
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(getString(R.string.app_name));
        actionBar.show();
    }

    private void loadData() {
        Log.d(LOG_LABEL, "load recipes into list");
        if (recipeListAdapter == null || recipeList.size() != recipeListAdapter.getItemCount()) {
            recipeListAdapter = new RecipeListAdapter(getContext(), recipeList, this);
            recipeListAdapter.submitList(recipeList);
        } else {
            // submit list for diff
            recipeListAdapter.submitList(recipeList);
        }

        recipeListRecyclerView.setAdapter(recipeListAdapter);
        recipeListAdapter.notifyDataSetChanged();
        recipeListRecyclerView.requestLayout();

        if (recipeList.isEmpty()) {
            recipeListRecyclerView.setVisibility(View.GONE);
            noDataTextView.setVisibility(View.VISIBLE);
            Log.w(LOG_LABEL, "Empty list");
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

        activity.goToStepList(recipe.getId());
    }
}
