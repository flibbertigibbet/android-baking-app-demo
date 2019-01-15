package com.banderkat.recipes.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.banderkat.recipes.BR;
import com.banderkat.recipes.R;
import com.banderkat.recipes.data.models.Step;
import com.banderkat.recipes.fragments.RecipeStepFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Step} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class RecipeStepAdapter extends ListAdapter<Step, RecipeStepAdapter.ViewHolder> {

    private List<Step> recipeSteps;
    private long recipeId;
    private OnListFragmentInteractionListener listener;
    private LayoutInflater inflater;

    private static final String PICASSO_GROUP = "step_thumbnails";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        private ViewHolder(ViewDataBinding binding, final OnListFragmentInteractionListener listener, long recipeId) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(v -> listener.onListFragmentInteraction(recipeId, getAdapterPosition()));
            this.binding = binding;
        }

        public void bind(Step step, Context context) {
            binding.setVariable(BR.step, step);
            binding.executePendingBindings();
        }
    }

    public RecipeStepAdapter(Context context, long recipeId, List<Step> items, OnListFragmentInteractionListener listener) {
        this();
        this.recipeSteps = items;
        this.recipeId = recipeId;
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
        return new RecipeStepAdapter.ViewHolder(binding, this.listener, this.recipeId);
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

    @BindingAdapter({"bind:thumbnailUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get().load(imageUrl).tag(PICASSO_GROUP).fit().into(view);
    }
}
