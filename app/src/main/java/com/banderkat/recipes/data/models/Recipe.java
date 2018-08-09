package com.banderkat.recipes.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Recipe {

    @PrimaryKey
    @NonNull
    private final String id;

    public Recipe(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
