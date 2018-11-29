package com.banderkat.recipes.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.arch.lifecycle.ViewModelStore;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.banderkat.recipes.data.models.Step;
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

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_RECIPE_ID = "recipe-id";

    @Inject
    public RecipeViewModelFactory viewModelFactory;
    RecipeViewModel viewModel;

    private int columnCount = 1;
    private long recipeId;
    private Recipe recipe;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeStepFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RecipeStepFragment newInstance(int columnCount, long recipeId) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putLong(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            columnCount = bundle.getInt(ARG_COLUMN_COUNT);
            recipeId = bundle.getLong(ARG_RECIPE_ID);

            Log.d(LOG_LABEL, "Getting recipe for steps list...");

            viewModel = ((RecipesMainActivity) getActivity()).getViewModel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_list, container, false);

        Log.d(LOG_LABEL, "create step fragment for recipe " + recipeId);

        RecyclerView recyclerView = view.findViewById(R.id.recipe_step_recycler_view);

        // Set the adapter
        Context context = view.getContext();

        if (columnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            Log.d(LOG_LABEL, "Column count is: " + columnCount);
            recyclerView.setLayoutManager(new GridLayoutManager(context, columnCount));
        }

        viewModel.getRecipe(recipeId).observe(this, foundRecipe -> {
            this.recipe = foundRecipe;
            Log.d(LOG_LABEL, "Got recipe " + recipe.getName());
            recyclerView.setAdapter(new RecipeStepAdapter(getContext(), recipe.getSteps(), mListener));
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Step item);
    }
}
