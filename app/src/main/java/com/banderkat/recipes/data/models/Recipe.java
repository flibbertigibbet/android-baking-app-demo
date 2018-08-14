package com.banderkat.recipes.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.banderkat.recipes.data.IngredientConverter;

import java.util.List;

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey
    @NonNull
    private final long id;

    private final String name;

    @TypeConverters(IngredientConverter.class)
    private List<Ingredient> ingredients;

    public Recipe(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
