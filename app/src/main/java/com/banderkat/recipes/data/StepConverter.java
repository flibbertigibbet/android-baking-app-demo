package com.banderkat.recipes.data;

import android.arch.persistence.room.TypeConverter;

import com.banderkat.recipes.data.models.Step;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StepConverter {

    @TypeConverter
    public String fromStepList(List<Step> steps) {
        if (steps == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>(){}.getType();
        return gson.toJson(steps, type);
    }

    @TypeConverter
    public List<Step> toStepList(String steps) {
        if (steps == null) {
            return null;
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Step>>(){}.getType();
        return gson.fromJson(steps, type);
    }
}
