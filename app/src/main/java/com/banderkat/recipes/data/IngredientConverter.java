package com.banderkat.recipes.data;

import android.arch.persistence.room.TypeConverter;

import com.banderkat.recipes.data.models.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public final class IngredientConverter {

    @TypeConverter
    public String fromIngredientList(List<Ingredient> ingredients) {
        if (ingredients == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>(){}.getType();
        return gson.toJson(ingredients, type);
    }

    @TypeConverter
    public List<Ingredient> toIngredientList(String ingredients) {
        if (ingredients == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Ingredient>>(){}.getType();

        // set ID to list offset
        List<Ingredient> ingredientList = gson.fromJson(ingredients, type);
        for (int i = 0; i < ingredientList.size(); i++) {
            Ingredient ingredient = ingredientList.get(i);
            ingredient.setId(i);
        }

        return ingredientList;
    }
}
