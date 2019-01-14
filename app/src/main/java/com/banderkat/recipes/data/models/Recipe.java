package com.banderkat.recipes.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.banderkat.recipes.data.IngredientConverter;
import com.banderkat.recipes.data.StepConverter;

import java.util.List;

@Entity(tableName = "recipe")
public class Recipe {

    @PrimaryKey
    @NonNull
    private final long id;

    private final String name;

    @TypeConverters(IngredientConverter.class)
    private List<Ingredient> ingredients;

    @TypeConverters(StepConverter.class)
    private List<Step> steps;

    // timestamp is not final, as it is set on database save, and not by serializer
    private long timestamp;

    public Recipe(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Timestamp entries. Timestamp value does not come from query result; it should be set
     * on database save. Gson serializer will initialize value to zero.
     *
     * @param timestamp Time in milliseconds since Unix epoch
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
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

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }
}
