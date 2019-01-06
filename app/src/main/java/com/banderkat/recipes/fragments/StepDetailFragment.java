package com.banderkat.recipes.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banderkat.recipes.BR;
import com.banderkat.recipes.R;
import com.banderkat.recipes.activities.RecipesMainActivity;
import com.banderkat.recipes.data.RecipeViewModel;
import com.banderkat.recipes.data.models.Ingredient;
import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.data.models.Step;
import com.banderkat.recipes.databinding.FragmentStepDetailBinding;
import com.banderkat.recipes.di.RecipeViewModelFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnStepDetailInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StepDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepDetailFragment extends Fragment {

    public static final String LOG_LABEL = "StepDetail";

    private static final String ARG_RECIPE_ID = "recipe-id";
    private static final String ARG_STEP_ID = "step-id";

    @Inject
    public RecipeViewModelFactory viewModelFactory;
    RecipeViewModel viewModel;

    private long recipeId;
    private int stepId;
    private Recipe recipe;
    private Step step;
    private RecipesMainActivity activity;
    private SimpleExoPlayer player;

    private OnStepDetailInteractionListener interactionListener;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    public static StepDetailFragment newInstance(long recipeId, int stepId) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_RECIPE_ID, recipeId);
        args.putInt(ARG_STEP_ID, stepId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (RecipesMainActivity) getActivity();

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            recipeId = bundle.getLong(ARG_RECIPE_ID);
            stepId = bundle.getInt(ARG_STEP_ID);
            viewModel = activity.getViewModel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_LABEL, "create step fragment for recipe " + recipeId);

        FragmentStepDetailBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_step_detail, container, false);
        View view = binding.getRoot();

        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        viewModel.getRecipe(recipeId).observe(this, foundRecipe -> {
            this.recipe = foundRecipe;
            Log.d(LOG_LABEL, "Got recipe for step detail: " + recipe.getName());

            step = recipe.getSteps().get(stepId);

            Log.d(LOG_LABEL, "Got step detail " + step.getShortDescription() + " for recipe " + recipe.getName());

            actionBar.setTitle(recipe.getName());
            binding.setVariable(BR.step, step);
            if (stepId == 0) {
                Log.d(LOG_LABEL, "Show ingredients on first step");
                List<Ingredient> ingredients = recipe.getIngredients();
                String ingredientsText = "";
                for (Ingredient ingredient : ingredients) {
                    ingredientsText += ingredient.getIngredient() + ": " +
                            ingredient.getQuantity() + ingredient.getMeasure() + "\n\n";
                }
                binding.stepDetailIngredients.setText(ingredientsText);
            } else {
                binding.stepDetailPrevBtn.setVisibility(View.VISIBLE);
            }

            if (stepId < recipe.getSteps().size() - 1) {
                binding.stepDetailNextBtn.setVisibility(View.VISIBLE);
            }

            PlayerView playerView = binding.stepDetailPlayerView;
            if (!step.getVideoURL().isEmpty()) {
                Context context = view.getContext();
                player = ExoPlayerFactory.newSimpleInstance(context);
                playerView.setPlayer(player);

                Uri videoUri = Uri.parse(step.getVideoURL());
                Log.d(LOG_LABEL, "Going to load video from " + videoUri.toString());
                // Produces DataSource instances through which media data is loaded.
                DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                        Util.getUserAgent(context, "yourApplicationName"));
                // This is the MediaSource representing the media to be played.
                MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(videoUri);

                // Prepare the player with the source.
                player.prepare(videoSource);
            } else {
                Log.d(LOG_LABEL, "No video for step " + step.getShortDescription());
                playerView.setVisibility(View.GONE);
            }
        });

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(LOG_LABEL, "in landscape");
            actionBar.hide();
        } else {
            Log.d(LOG_LABEL, "in portrait");
            actionBar.show();
        }

        // Set handlers for prev/next buttons
        binding.stepDetailPrevBtn.setOnClickListener(v -> {
            Log.d(LOG_LABEL, "Go to previous step");
            activity.goToRecipeStepDetail(recipeId, stepId - 1);
        });

        binding.stepDetailNextBtn.setOnClickListener(v -> {
            Log.d(LOG_LABEL, "Go to next step");
            activity.goToRecipeStepDetail(recipeId, stepId + 1);
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(ARG_RECIPE_ID, recipeId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepDetailInteractionListener) {
            interactionListener = (OnStepDetailInteractionListener) context;
        } else {
            Log.d(LOG_LABEL, "TODO: implement step detail interaction listener");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(LOG_LABEL, "onStop");
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    public void onDetach() {
        Log.d(LOG_LABEL, "onDetach");
        super.onDetach();
        interactionListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnStepDetailInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
