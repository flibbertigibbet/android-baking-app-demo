package com.banderkat.recipes.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "ingredient", foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "id", childColumns = "recipeId", onDelete = CASCADE),
        primaryKeys = {"id", "recipeId"})
public class Ingredient {

    @ColumnInfo(index = true)
    private long id;

    @ColumnInfo(index = true)
    private long recipeId;

    private final float quantity;

    private final String measure;

    private final String ingredient;

    public Ingredient(long recipeId, float quantity, String measure, String ingredient) {
        this.recipeId = recipeId;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
