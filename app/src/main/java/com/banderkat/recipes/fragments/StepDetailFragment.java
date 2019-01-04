package com.banderkat.recipes.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.banderkat.recipes.BR;
import com.banderkat.recipes.R;
import com.banderkat.recipes.activities.RecipesMainActivity;
import com.banderkat.recipes.data.RecipeViewModel;
import com.banderkat.recipes.data.models.Recipe;
import com.banderkat.recipes.data.models.Step;
import com.banderkat.recipes.di.RecipeViewModelFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

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

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            recipeId = bundle.getLong(ARG_RECIPE_ID);
            stepId = bundle.getInt(ARG_STEP_ID);
            viewModel = ((RecipesMainActivity) getActivity()).getViewModel();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(LOG_LABEL, "create step fragment for recipe " + recipeId);

        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_step_detail, container, false);
        View view = binding.getRoot();

        viewModel.getRecipe(recipeId).observe(this, foundRecipe -> {
            this.recipe = foundRecipe;
            Log.d(LOG_LABEL, "Got recipe for step detail: " + recipe.getName());

            step = recipe.getSteps().get(stepId);

            Log.d(LOG_LABEL, "Got step detail " + step.getShortDescription() + " for recipe " + recipe.getName());
            getActivity().setTitle(recipe.getName());
            binding.setVariable(BR.step, step);

            PlayerView playerView = view.findViewById(R.id.step_detail_player_view);
            if (!step.getVideoURL().isEmpty()) {
                Context context = view.getContext();
                SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(context);
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
    public void onDetach() {
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
