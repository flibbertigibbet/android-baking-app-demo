package com.banderkat.recipes.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "step", foreignKeys = @ForeignKey(entity = Recipe.class,
        parentColumns = "id", childColumns = "recipeId", onDelete = CASCADE),
        primaryKeys = {"id", "recipeId"})
public class Step {

    @ColumnInfo(index = true)
    private final long id;

    @ColumnInfo(index = true)
    private long recipeId;

    private final String shortDescription;

    private final String description;

    private final String videoURL;

    private final String thumbnailURL;

    public Step(long id, long recipeId, String shortDescription, String description,
                String videoURL, String thumbnailURL) {

        this.id = id;
        this.recipeId = recipeId;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
    }

    public long getId() {
        return id;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
