package com.banderkat.recipes.adapters;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.banderkat.recipes.BR;
import com.banderkat.recipes.R;
import com.banderkat.recipes.data.models.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeListAdapter extends ListAdapter<Recipe, RecipeListAdapter.ViewHolder> {

    public interface RecipeListItemClickListener {
        void clickedRecipe(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        private ViewHolder(ViewDataBinding binding, final RecipeListItemClickListener listener) {
            super(binding.getRoot());
            binding.getRoot().setOnClickListener(v -> listener.clickedRecipe(getAdapterPosition()));
            this.binding = binding;
        }

        public void bind(Recipe recipe, Context context) {
            binding.setVariable(BR.recipe, recipe);
            binding.executePendingBindings();
        }
    }

    private static final String PICASSO_GROUP = "recipe_images";

    private Context context;
    private LayoutInflater inflater;
    private List<Recipe> recipeList;
    private RecipeListItemClickListener listener;

    public RecipeListAdapter(Context context, List<Recipe> recipeList, RecipeListItemClickListener listener) {
        this();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.recipeList = recipeList;
        this.listener = listener;
    }

    private RecipeListAdapter() {
        super(new DiffUtil.ItemCallback<Recipe>() {

            @Override
            public boolean areItemsTheSame(Recipe oldItem, Recipe newItem) {
                // Returns true if these are for the same attraction; properties may differ.
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(Recipe oldItem, Recipe newItem) {
                return oldItem.equals(newItem);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.recipe_list_item, parent, false);
        return new ViewHolder(binding, this.listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.bind(recipe, holder.itemView.getContext());
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get().setLoggingEnabled(true);
        Log.d("BindImageUrl", imageUrl);
        Picasso.get().load(imageUrl).tag(PICASSO_GROUP).fit().into(view);
    }
}
