package com.banderkat.recipes.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banderkat.recipes.R;
import com.banderkat.recipes.activities.RecipesMainActivity;
import com.banderkat.recipes.adapters.RecipeStepAdapter;
import com.banderkat.recipes.data.RecipeViewModel;
import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.di.RecipeViewModelFactory;

import javax.inject.Inject;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RecipeStepFragment extends Fragment {

    public static final String LOG_LABEL = "StepFragment";

    private static final String ARG_RECIPE_ID = "recipe-id";

    @Inject
    public RecipeViewModelFactory viewModelFactory;
    RecipeViewModel viewModel;

    private long recipeId;
    private Recipe recipe;
    private OnListFragmentInteractionListener interactionListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepFragment() {
    }

    public static RecipeStepFragment newInstance(long recipeId) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            recipeId = bundle.getLong(ARG_RECIPE_ID);
            viewModel = ((RecipesMainActivity) getActivity()).getViewModel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_LABEL, "create step fragment for recipe " + recipeId);

        View view = inflater.inflate(R.layout.fragment_step_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recipe_step_recycler_view);

        // Set the adapter
        Context context = view.getContext();

        int columnCount = ((RecipesMainActivity) getActivity()).getGridSpanCount();
        if (columnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            Log.d(LOG_LABEL, "Column count is: " + columnCount);
            recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        }

        viewModel.getRecipe(recipeId).observe(this, foundRecipe -> {
            this.recipe = foundRecipe;
            Log.d(LOG_LABEL, "Got recipe " + recipe.getName());
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            actionBar.setTitle(recipe.getName());
            actionBar.show();
            recyclerView.setAdapter(new RecipeStepAdapter(getContext(), recipeId, recipe.getSteps(), interactionListener));
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_LABEL, "onSaveInstanceState");
        outState.putLong(ARG_RECIPE_ID, recipeId);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG_LABEL, "step list attaching");
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            interactionListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(LOG_LABEL, "step list detaching");
        super.onDetach();
        interactionListener = null;
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(long recipeId, int position);
    }
}
