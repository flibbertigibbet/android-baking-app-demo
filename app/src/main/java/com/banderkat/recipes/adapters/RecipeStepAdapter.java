package com.banderkat.recipes.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.banderkat.recipes.BR;
import com.banderkat.recipes.R;
import com.banderkat.recipes.data.models.Step;
import com.banderkat.recipes.fragments.RecipeStepFragment.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Step} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class RecipeStepAdapter extends ListAdapter<Step, RecipeStepAdapter.ViewHolder> {

    private List<Step> recipeSteps;
    private OnListFragmentInteractionListener listener;
    private LayoutInflater inflater;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        private ViewHolder(ViewDataBinding binding, final OnListFragmentInteractionListener listener) {
            super(binding.getRoot());
            // FIXME: how to get at step item from here for getAdapterPosition() ?
            binding.getRoot().setOnClickListener(v -> listener.onListFragmentInteraction(null));
            this.binding = binding;
        }

        public void bind(Step step, Context context) {
            binding.setVariable(BR.step, step);
            Log.d("stepadapter", step.getDescription());
            binding.executePendingBindings();
        }
    }

    public RecipeStepAdapter(Context context, List<Step> items, OnListFragmentInteractionListener listener) {
        this();
        this.recipeSteps = items;
        this.listener = listener;

        this.inflater = LayoutInflater.from(context);
    }

    private RecipeStepAdapter() {
        super(new DiffUtil.ItemCallback<Step>() {

            @Override
            public boolean areItemsTheSame(Step oldItem, Step newItem) {
                // Returns true if these are for the same attraction; properties may differ.
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(Step oldItem, Step newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.recipe_step_item, parent, false);
        return new RecipeStepAdapter.ViewHolder(binding, this.listener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Step step = recipeSteps.get(position);
        holder.bind(step, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return recipeSteps.size();
    }
}
